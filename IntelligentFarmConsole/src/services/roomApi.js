import { post, get } from "@/utils/request";

export const apiAddRoom = (data) => post("/api/room/add", data);

export const apiEditRoom = (data) => post("/api/room/edit", data);

export const apiRoomList = (data) => post("/api/room/list", data);

export const apiRoomDelete = (data) => get("/api/room/delete", data);

export const apiRoomDeleteBatch = (data) => post("/api/room/deleteBatch", data);

export const apiRoomCheckIn = (data) => post("/api/room/checkIn", data);

export const apiRoomBatchUpdateCapacity = (data) =>
  post("/api/room/batchUpdateCapacity", data);
