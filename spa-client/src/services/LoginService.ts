import { paths } from "../common/constants";
import { authedFetch } from "../scripts/authedFetch";
import { checkError } from "../scripts/ErrorChecker";
import { ErrorResponse, Result, UserModel } from "../types";

export class LoginService {
  public async login(username: string, password: string):Promise<Result<UserModel>> {
    const credentials = username + ":" + password;

    const hash = btoa(credentials);
    try {
      const response = await fetch(paths.BASE_URL + "users/3", {
        // TODO: Remplazar este id hardcodeado por el endpoint /user cuando este disponible
        method: "GET",
        headers: {
          Authorization: "Basic as" + hash,
        },
      });

      const parsedResponse = await checkError<UserModel>(response);
      parsedResponse.token = response.headers
        .get("Authorization")
        ?.toString()
        .split(" ")[1];

      return Result.ok(parsedResponse as UserModel);
    } catch (error: any) {
      return Result.failed(new ErrorResponse(parseInt(error), error));
    }
  }
}