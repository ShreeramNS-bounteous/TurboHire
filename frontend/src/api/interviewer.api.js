import api from "./axios";

export const getMyAvailabilitySlots = async (userId) => {
  const res = await api.get(`/api/interviewers/${userId}/slots`);
  return res.data;
};

export const addAvailabilitySlot = async (userId, payload) => {
  const res = await api.post(
    `/api/interviewers/${userId}/slots`,
    payload
  );
  return res.data;
};

export const deleteAvailabilitySlot = async (slotId) => {
  await api.delete(`/api/interviewers/slots/${slotId}`);
};

export const fetchAvailableInterviewers = ({ date, from, to }) => {
  return api.get("/api/interviewers/availability", {
    params: { date, from, to }
  });
};

/* 🔥 NEW - Interviewer Dashboard */
export const getMyInterviews = async () => {
  const res = await api.get("/api/interviewers/my-interviews");
  return res.data;
};

export const submitInterviewFeedback = async (
  interviewId,
  payload
) => {
  const res = await api.post(
    `/api/interviews/${interviewId}/feedback`,
    payload
  );
  return res.data;
};

export const getMyInterviewerProfile = async () => {
  const res = await api.get("/api/interviewers/me");
  return res.data;
};

export const getPreviousRoundFeedback = async (interviewId) => {
  const res = await api.get(
    `/api/interviews/feedback/${interviewId}/previous`
  );
  return res.data;
};

export const markInterviewAttendance = async (interviewId, attendanceStatus) => {
  return api.post(`/api/interviews/${interviewId}/attendance`, {
    attendanceStatus,
  });
};





