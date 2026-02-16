import api from "./axios";

export const loginApi = async (email, password) => {
  const res = await api.post("/api/auth/login", {
    email,
    password,
  });
  console.log(res.data.accessToken)
  return res.data.accessToken; // { accessToken }
};
