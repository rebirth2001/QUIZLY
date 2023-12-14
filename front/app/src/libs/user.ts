import axios, { AxiosError } from "axios";
import { API_BASE } from "./backend.binding";

export type RegisterRequest = {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
};

export type AuthenticateRequest = {
  email: string;
  password: string;
};

export type AuthenticateResponse = {
  success: boolean;
  accessToken: string;
};

export class User {
  private accessToken: string;
  private username: string;
  private email: string;

  constructor(access_token: string) {
    this.accessToken = access_token;
    this.username = "";
    this.email = "";
  }

  public async updateUserDetails() {
    if (this.username.length > 0 && this.email.length > 0) {
      return;
    }

    try {
      const response = await axios.get(`${API_BASE}/auth/details`, {
        headers: this.getRequestHeader(),
      });
      this.username = response.data?.username;
      this.email = response.data?.email;
    } catch (error) {
      console.log(error);
    }
    console.log(this.accessToken, this.email, this.username);
  }

  private getRequestHeader(): any {
    const header: any = {};
    header["Content-Type"] = "application/json";
    if (this.accessToken.length != 0) {
      header["Authorization"] = `Bearer ${this.accessToken}`;
    }
    return header;
  }

  public getUsername() {
    return this.username;
  }
  public getEmail() {
    return this.email;
  }
}

export function validateRegisterForm(
  rr: RegisterRequest,
  setErrorMsg: (value: React.SetStateAction<string>) => void
): boolean {
  setErrorMsg("");
  if (
    rr.username.length == 0 ||
    rr.email.length == 0 ||
    rr.password.length == 0 ||
    rr.confirmPassword.length == 0
  ) {
    setErrorMsg("All form fields must be filled.");
    return false;
  }

  const usernameRegex = /^\w+$/;
  if (!usernameRegex.test(rr.username)) {
    setErrorMsg("Username must contain only letters, numbers and underscores!");
    return false;
  }

  const regexResult = rr.email
    .toLowerCase()
    .match(
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    );

  if (!regexResult) {
    setErrorMsg("Please use a valid email.");
    return false;
  }

  const passwordRegex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/;
  if (!passwordRegex.test(rr.password)) {
    setErrorMsg(
      "Password must be at least 8 characters long, and contains 1 number, 1 lowercase and 1 uppercase character."
    );
    return false;
  }

  if (rr.password != rr.confirmPassword) {
    setErrorMsg("Passwords don't match");
    return false;
  }

  return true;
}

export async function registerUser(
  request: RegisterRequest,
  setErrorMsg: (value: React.SetStateAction<string>) => void
): Promise<boolean> {
  try {
    await axios.post(`${API_BASE}/auth/sign-up`, JSON.stringify(request), {
      headers: { "Content-Type": "application/json" },
      withCredentials: false,
    });
    return true;
    // navigate("/sign-in");
  } catch (err: any) {
    if (err instanceof AxiosError) {
      if (err.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        setErrorMsg(JSON.stringify(err.response.data.errors[0]));
      } else if (err.request) {
        setErrorMsg("Couldn't reach the server, please try again.");
      } else {
        setErrorMsg("Error setting up the request: " + err.message);
      }
    } else {
      setErrorMsg("Unexpected error: " + err.message);
    }
  }
  return false;
}

export async function authenticateUser(
  request: AuthenticateRequest
): Promise<AuthenticateResponse | null> {
  try {
    const response = await axios.post(
      `${API_BASE}/auth/sign-in`,
      JSON.stringify(request),
      {
        headers: { "Content-Type": "application/json" },
        withCredentials: false,
      }
    );
    const authResp: AuthenticateResponse = {
      success: false,
      accessToken: "",
    };
    if (response.status == 200) {
      authResp.success = true;
    }
    const accessToken = response?.data?.accessToken;
    if (accessToken !== null && accessToken !== "null") {
      authResp.accessToken = accessToken;
    }
    return authResp;
  } catch (err) {
    console.error(err);
  }
  return null;
}
