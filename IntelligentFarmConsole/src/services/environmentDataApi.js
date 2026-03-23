import { post } from "@/utils/request";

export const apiEnvironmentDataList = (data) =>
  post("/environmentData/list", data);
