import { paths } from "../common/constants";
import { checkError } from "../scripts/ErrorChecker";
import { authedFetch } from "../scripts/authedFetch";
import {
  Result,
  CourseModel,
  ErrorResponse,
  AnnouncementModel,
  UserModel,
  ExamModel,
} from "../types";
import AnswerModel from "../types/AnswerModel";
import { getFetch } from "../scripts/getFetch";

export class CourseService {
  private readonly basePath = paths.BASE_URL + paths.COURSES;

  public async getCourseById(courseId: number): Promise<Result<CourseModel>> {
    return getFetch<CourseModel>(this.basePath + courseId);
  }

  public async getCourses(
    page?: number,
    pageSize?: number
  ): Promise<Result<CourseModel[]>> {
    let url = new URL(this.basePath);
    let params = new URLSearchParams();
    if (typeof page !== "undefined") {
      url = new URL(paths.BASE_URL + paths.COURSES_QUERY_PARAMS);
      params.append("page", page.toString());
    }

    if (typeof pageSize !== "undefined") {
      url = new URL(paths.BASE_URL + paths.COURSES_QUERY_PARAMS);
      params.append("pageSize", pageSize.toString());
      url.search = params.toString();
    }
    return getFetch<CourseModel[]>(url.toString());
  }

  public async getHelpers(courseId: number): Promise<Result<UserModel>> {
    return getFetch<UserModel>(this.basePath + courseId + "/helpers");
  }

  public async getTeachers(courseId: number): Promise<Result<UserModel>> {
    return getFetch<UserModel>(this.basePath + courseId + "/teachers");
  }

  public async getStudents(courseId: number): Promise<Result<UserModel>> {
    return getFetch<UserModel>(this.basePath + courseId + "/students");
  }
  public async getExams(courseId: number): Promise<Result<ExamModel>> {
    return getFetch<ExamModel>(this.basePath + courseId + "/exams");
  }

  public async getSolvedExams(courseId: number): Promise<Result<ExamModel>> {
    return getFetch<ExamModel>(this.basePath + courseId + "/exams/solved");
  }

  public async getUnsolvedExams(courseId: number): Promise<Result<ExamModel>> {
    return getFetch<ExamModel>(this.basePath + courseId + "/exams/unsolved");
  }

  public async getCourseAnswers(
    courseId: number
  ): Promise<Result<AnswerModel[]>> {
    // TODO: Esperar respuesta mail para ver que hacer con los links en este caso
    return getFetch<AnswerModel[]>(this.basePath + courseId + "/exams/answers");
  }

  //TODO: Ver si este service puede mapear el json sin el type! (cuando podamos correr la api)
  public async getAvailableYears(): Promise<Result<number>> {
    return getFetch<number>(this.basePath + "available-years");
  }

  public async getAnnouncemets(
    courseId: number
  ): Promise<Result<AnnouncementModel[]>> {
    return getFetch<AnnouncementModel[]>(
      this.basePath + courseId + "/announcements"
    );
  }
}