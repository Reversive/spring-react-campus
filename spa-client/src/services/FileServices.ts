import { paths } from "../common/constants";
import { authedFetch } from "../scripts/authedFetch";
import { fileUrlMaker } from "../scripts/fileUrlMaker";
import { getPagedFetch } from "../scripts/getPagedFetch";
import { ErrorResponse, FileModel, PagedContent, Result } from "../types";

export class FileService {
  private readonly basePath = paths.BASE_URL + paths.FILES;

  public async getFileById(fileId: number): Promise<Result<File>> {
    try {
      let response = await authedFetch(this.basePath + "/" + fileId, {
        method: "GET",
      });

      let dispositionHeader = response.headers.get("Content-Disposition");

      if (dispositionHeader == null) {
        dispositionHeader = "";
      }
      let fileName = "";
      let fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
      let matches = fileNameRegex.exec(dispositionHeader);
      if (matches != null && matches[1]) {
        fileName = matches[1].replace(/['"]/g, "");
      }

      return Result.ok(new File([await response.blob()], fileName));
    } catch (err: any) {
      return Result.failed(
        new ErrorResponse(parseInt(err.message), err.message)
      );
    }
  }

  public async getFiles(
    categoryType?: number[],
    extensionType?: number[],
    query?: string,
    orderProperty?: string,
    orderDirection?: string,
    page?: number,
    pageSize?: number
  ): Promise<Result<PagedContent<FileModel[]>>> {
    let url = fileUrlMaker(
      this.basePath,
      categoryType,
      extensionType,
      query,
      orderProperty,
      orderDirection,
      page,
      pageSize
    );
    return getPagedFetch<FileModel[]>(url.toString());
  }

  public async deleteFile(fileId: number) {
    return authedFetch(this.basePath + "/" + fileId, {
      method: "DELETE",
    });
  }
}