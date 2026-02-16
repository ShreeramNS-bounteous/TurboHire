import api from "./axios";


export const fetchPendingInterviews = (jobId) => {
  if (jobId) {
    return api
      .get(`/api/interviews/to-be-scheduled?jobId=${jobId}`)
      .then(res => res.data);
  }

  return api
    .get(`/api/interviews/to-be-scheduled`)
    .then(res => res.data);
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

export const fetchScheduledInterviews = (jobId) => {
  if (jobId) {
    return api
      .get(`/api/interviews/scheduled?jobId=${jobId}`)
      .then(res => res.data);
  }

  return api
    .get(`/api/interviews/scheduled`)
    .then(res => res.data);
};


export const fetchCompletedInterviews = (jobId) => {
  if (jobId) {
    return api
      .get(`/api/interviews/completed?jobId=${jobId}`)
      .then(res => res.data);
  }

  return api
    .get(`/api/interviews/completed`)
    .then(res => res.data);
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