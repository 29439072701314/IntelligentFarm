import { post, get } from "@/utils/request";

export const apiAddRoom = (data) => post("/room/add", data);

export const apiEditRoom = (data) => post("/room/edit", data);

export const apiRoomList = (data) => post("/room/list", data);

export const apiRoomDelete = (data) => get("/room/delete", data);

export const apiRoomDeleteBatch = (data) => post("/room/deleteBatch", data);

export const apiRoomCheckIn = (data) => post("/room/checkIn", data);

export const apiRoomBatchUpdateCapacity = (data) =>
  post("/room/batchUpdateCapacity", data);
