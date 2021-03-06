import { paths } from "../common/constants";
import { checkError } from "../scripts/ErrorChecker";
import { ErrorResponse, Result, UserModel } from "../types";
import { setCookie } from "../scripts/cookies";

export class LoginService {
  public async login(
    username: string,
    password: string
  ): Promise<Result<UserModel>> {
    const credentials = username + ":" + password;
    const hash = btoa(credentials);
    setCookie("basic-token", hash, 7);
    try {
      const response = await fetch(paths.BASE_URL + "/user", {
        method: "GET",
        headers: {
          Authorization: "Basic " + hash,
        },
      });

      const parsedResponse = await checkError<UserModel>(response);
      parsedResponse.token = response.headers
        .get("Authorization")
        ?.toString()
        .split(" ")[1];

      return Result.ok(parsedResponse as UserModel);
    } catch (error: any) {
      return Result.failed(
        new ErrorResponse(parseInt(error.message), error.message)
      );
    }
  }
}
