import { ThemeProvider } from "styled-components";
import { AuthProvider } from "./contexts/AuthContext";
import React from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import MainLayout from "./components/layouts/MainLayout";
import Portal from "./pages/Portal";
import Timetable from "./pages/Timetable";
import Announcements from "./pages/Announcements";
import Files from "./pages/Files";
import User from "./pages/User";
import Custom404 from "./pages/404";
import Mail from "./pages/courses/Mail";
import CourseLayout from "./components/layouts/CourseLayout";
import CourseAnnouncements from "./pages/courses/CourseAnnouncements";
import CourseFiles from "./pages/courses/CourseFiles";
import CourseTeachers from "./pages/courses/CourseTeachers";
import CourseSchedule from "./pages/courses/CourseSchedule";
import Login from "./pages/Login";
import RequireAuth from "./components/RequireAuth";
import CourseExams from "./pages/courses/CourseExams";
import CourseExamStandalone from "./pages/courses/CourseExams/CourseExamStandalone";
import CorrectExam from "./pages/courses/CourseExams/CourseExamStandalone/CorrectExam";
import AdminPortal from "./pages/admin/AdminPortal";
import AdminAllCourses from "./pages/admin/AdminAllCourses";
import AdminLayout from "./components/layouts/AdminLayout";
import AdminNewUser from "./pages/admin/AdminNewUser";
import AdminSelectCourse from "./pages/admin/AdminSelectCourse";
import AdminNewCourse from "./pages/admin/AdminNewCourse";
import AdminAddUserToCourse from "./pages/admin/AdminAddUserToCourse";

const theme = {
  cyanDarkest: "#176961",
  cyanDark: "#2EC4B6",
  cyanLight: "#CBF3F0",
  successBack: "#45FF9F",
  successFront: "#39614C",
};

function App() {
  return (
    <ThemeProvider theme={theme}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route
              path="/"
              element={
                <RequireAuth>
                  <MainLayout />
                </RequireAuth>
              }
            >
              <Route index element={<Navigate to={"/portal"} />} />
              <Route path="portal" element={<Portal />} />
              <Route path="timetable" element={<Timetable />} />
              <Route path="announcements" element={<Announcements />} />
              <Route path="files" element={<Files />} />
              <Route path="user" element={<User />} />
              <Route path="*" element={<Custom404 />} />
              <Route path="course/:courseId/mail/:mailId" element={<Mail />} />
              <Route path="course/:courseId" element={<CourseLayout />}>
                <Route index element={<Navigate to="announcements" />} />
                <Route path="announcements" element={<CourseAnnouncements />} />
                <Route path="files" element={<CourseFiles />} />
                <Route path="exams" element={<CourseExams />} />
                <Route path="exam/:examId" element={<CourseExamStandalone />} />
                <Route
                  path="exam/:examId/answer/:answerId/correct"
                  element={<CorrectExam />}
                />
                <Route path="teachers" element={<CourseTeachers />} />
                <Route path="schedule" element={<CourseSchedule />} />
              </Route>
              <Route path="admin" element={<AdminPortal />} />
            </Route>
            <Route path="admin" element={<AdminLayout />}>
              <Route path="user/new" element={<AdminNewUser />} />
              <Route path="course/all" element={<AdminAllCourses />} />
              <Route path="course/select" element={<AdminSelectCourse />} />
              <Route path="course/new" element={<AdminNewCourse />} />
              <Route path="course/enroll" element={<AdminAddUserToCourse />} />
            </Route>
            <Route path="/login" element={<Login />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;