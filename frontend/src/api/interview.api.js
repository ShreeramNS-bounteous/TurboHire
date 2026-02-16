import api from "./axios";


export const fetchPendingInterviews = async () => {
  const res = await api.get("/api/interviews/to-be-scheduled");
  return res.data;
};

export const createInterview = async (candidateJobId) => {
  const res = await api.post("/api/interviews", {
    candidateJobId,
  });

  return res.data;
};

export const bookInterviewSlot = async (interviewId, payload) => {
  await api.post(`/api/interviews/${interviewId}/book-slot`, {
    interviewerSlotId: payload.interviewerSlotId,
    meetingUrl: payload.meetingUrl
  });
};

export const fetchScheduledInterviews = async () => {
  const res = await api.get("/api/interviews/scheduled");
  return res.data;
};

export const fetchCompletedInterviews = async () => {
  const res = await api.get("/api/interviews/completed");
  return res.data;
};



export const moveToNextRound = async (interviewId) => {
  return api.post(`/api/interviews/${interviewId}/move-next`);
};

export const hireCandidate = async (interviewId) => {
  return api.post(`/api/interviews/${interviewId}/hire`);
};

export const rejectCandidate = async (interviewId) => {
  return api.post(`/api/interviews/${interviewId}/reject`);
};