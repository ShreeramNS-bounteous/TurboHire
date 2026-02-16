import api from "./axios";

export const getBusinessUnits = async () => {
  const res = await api.get("/api/business-units");
  return res.data;
};

export const createBusinessUnit = async (name) => {
  const res = await api.post("/api/business-units", {
    name,
  });
  return res.data;
};
