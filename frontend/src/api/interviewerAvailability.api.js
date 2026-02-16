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
