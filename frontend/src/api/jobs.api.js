import api from "./axios";

export const getJobs = async () => {
  const res = await api.get("/api/jobs");
  return res.data;
};

export const publishJob = async (jobId) => {
  return api.put(`/api/jobs/${jobId}/publish`);
};

export const closeJob = async (jobId) => {
  return api.put(`/api/jobs/${jobId}/close`);
};

export const deleteJob = async (jobId) => {
  return api.delete(`/api/jobs/${jobId}`);
};

export const updateJob = async (jobId, payload) => {
  return api.put(`/api/jobs/${jobId}`, payload);
};
