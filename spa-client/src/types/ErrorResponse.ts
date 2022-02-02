export default class ErrorResponse {
  private readonly code: number;
  private readonly descritpion: string;

  public constructor(code: number, description: string) {
    this.code = code;
    this.descritpion = description;
  }

  public getCode(): number {
    return this.code;
  }

  public getDescription(): string {
    return this.descritpion;
  }
}