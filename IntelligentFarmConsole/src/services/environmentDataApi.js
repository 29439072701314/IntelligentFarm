import { post } from "@/utils/request";

export const apiEnvironmentDataList = (data) =>
  post("/api/environmentData/list", data);
