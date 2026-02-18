import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import { Toaster } from "react-hot-toast";
import ProtectedRoute from "./auth/ProtectedRoute";
import RecruiterLayout from "./layout/RecruiterLayout";
import RecruiterDashboard from "./pages/recruiter/RecruiterDashboard";
import BusinessUnits from "./pages/recruiter/BusinessUnits";
import Jobs from "./pages/recruiter/Jobs";
import CreateJob from "./pages/recruiter/CreateJob";
import Pipeline from "./pages/recruiter/Pipeline";
import AddNewCandidate from "./pages/recruiter/AddNewCandidate";
import AddExistingCandidate from "./pages/recruiter/AddExistingCandidate";
import Candidates from "./pages/recruiter/Candidates";
import JobsDashboard from "./pages/recruiter/jobs/JobsDashboard";
import InterviewerDashboard from "./pages/Interviewer/InterviewerDashboard";
import InterviewDashboard from "./pages/recruiter/Interview/InterviewDashboard";
import LandingPage from "./pages/LandingPage";
import InterviewDetailPage from "./pages/Interviewer/InterviewerDetailPage";

function App() {
  return (
    <BrowserRouter>
      <Toaster
        position="top-right"
        reverseOrder={false}
        toastOptions={{
          duration: 3000,
          style: {
            borderRadius: "10px",
            background: "#fff",
            color: "#1e293b",
          },
        }}
      />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route index element={<LandingPage />} />
        <Route
          path="/recruiter"
          element={
            <ProtectedRoute roles={["ADMIN", "RECRUITER"]}>
              <RecruiterLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<RecruiterDashboard />} />
          <Route path="business-units" element={<BusinessUnits />} />
          <Route path="jobs" element={<JobsDashboard />} />
          <Route path="/recruiter/jobs/create" element={<CreateJob />} />
          <Route path="pipeline" element={<Pipeline />} />
          <Route path="pipeline/:jobId/add-new" element={<AddNewCandidate />} />
          <Route path="/recruiter/candidates" element={<Candidates />} />
          <Route
            path="pipeline/:jobId/add-existing"
            element={<AddExistingCandidate />}
          />
          <Route path="interviews" element={<InterviewDashboard />} />
          <Route path="interviews/:jobId" element={<InterviewDashboard />} />
        </Route>

        <Route
          path="/interviewer"
          element={
            <ProtectedRoute roles={["USER"]}>
              <InterviewerDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/interviewer/interview/:id"
          element={
            <ProtectedRoute roles={["USER"]}>
              <InterviewDetailPage/>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <ProtectedRoute roles={["ADMIN"]}>
              <div>Admin Dashboard</div>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
