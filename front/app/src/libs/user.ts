import axios, { AxiosResponse } from "axios";
import { API_BASE } from "./backend.binding";

export class User {
  private accessToken: string;
  private username: string;
  private refreshToken: string;
  private expiration: number; // expect a value in ms
  private logOutTimer: number;
  private isExpired: boolean;

  constructor(
    accessToken: string,
    refreshToken: string,
    username: string,
    expiration: number
  ) {
    this.accessToken = accessToken;
    this.username = username;
    this.refreshToken = refreshToken;
    this.expiration = expiration;
    this.isExpired = false;
    this.logOutTimer = setTimeout(
      this.setIsExpired.bind(this),
      this.expiration
    );
    setTimeout(this.RefreshJwtToken.bind(this), this.expiration - 300_000);
  }

  private setRefreshTimer() {
    console.log(this);
    setTimeout(this.RefreshJwtToken.bind(this), this.expiration - 300_000); // calls refreshToken after every 10 min.
    clearTimeout(this.logOutTimer);
    this.logOutTimer = setTimeout(
      this.setIsExpired.bind(this),
      this.expiration
    ); // calls every 15 min
  }

  private RefreshJwtToken() {
    try {
      axios
        .post(
          `${API_BASE}/auth/refresh-token`,
          {
            refreshToken: this.refreshToken,
          },
          {
            headers: this.getRequestHeader(),
          }
        )
        .then((resp: AxiosResponse<any, any>) => {
          if (resp.status != 200) {
            throw Error("Failed To refresh accesstoken");
          }
          this.setAccessToken(resp.data?.accessToken);
          this.setRefreshToken(resp.data?.refreshToken);
          this.setRefreshTimer(); // restart a refresh timer.
        })
        .catch((reason: any) => {
          console.log(reason);
          throw Error("Couldn't connect to the server");
        });
    } catch (e) {
      console.log(e);
    }
  }

  private setAccessToken(value: string) {
    this.accessToken = value;
  }

  private setRefreshToken(value: string) {
    this.refreshToken = value;
  }

  private setIsExpired() {
    console.log("User JWT token has expired!!!!!!!!!!!");
    this.isExpired = true;
  }

  public isValid(): boolean {
    console.log(this);
    return !this.isExpired;
  }

  // public async updateUserDetails() {
  //   if (this.username.length > 0 && this.refreshToken.length > 0) {
  //     return;
  //   }

  //   try {
  //     const response = await axios.get(`${API_BASE}/auth/details`, {
  //       headers: this.getRequestHeader(),
  //     });
  //     this.username = response.data?.username;
  //     this.email = response.data?.email;
  //   } catch (error) {
  //     console.log(error);
  //   }
  //   console.log(this.accessToken, this.email, this.username);
  // }

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
}
