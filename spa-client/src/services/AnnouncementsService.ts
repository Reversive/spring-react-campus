import { paths } from "../common/constants";
import { AnnouncementModel, PagedContent, Result } from "../types";
import { getPagedFetch } from "../scripts/getPagedFetch";
import { pageUrlMaker } from "../scripts/pageUrlMaker";
import { authedFetch } from "../scripts/authedFetch";
import { resultFetch } from "../scripts/resultFetch";
import { parseAnnouncementResponse } from "../scripts/parseAnnouncementResponse";

export class AnnouncementsService {
  private readonly basePath = paths.BASE_URL + paths.ANNOUNCEMENTS;
  public async getAnnouncements(
    page?: number,
    pageSize?: number
  ): Promise<Result<PagedContent<AnnouncementModel[]>>> {
    let url = pageUrlMaker(this.basePath, page, pageSize);
    const resp = await getPagedFetch<AnnouncementModel[]>(url.toString());
    return parseAnnouncementResponse(resp);
  }

  public async getAnnouncementById(
    announcementId: number
  ): Promise<Result<AnnouncementModel>> {
    const resp = await resultFetch<AnnouncementModel>(
      this.basePath + "/" + announcementId,
      { method: "GET" }
    );

    if (!resp.hasFailed()) {
      resp.getData().date = new Date(resp.getData().date);
    }
    return resp;
  }

  public async deleteAnnouncement(announcementId: number) {
    return authedFetch(this.basePath + "/" + announcementId, {
      method: "DELETE",
    });
  }
}
