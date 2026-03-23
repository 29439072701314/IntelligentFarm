import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: {
    avatar: "",
    birthday: "",
    gender: {},
    phone: "",
    role: {},
    userId: 0,
    userName: "",
  },
  reducers: {
    setUser: (_state, action) => {
      return action.payload;
    },
    setAvatar: (state, action) => {
      state.avatar = action.payload;
    },
    setUserName: (state, action) => {
      state.userName = action.payload;
    },
  },
});
export default userSlice;
