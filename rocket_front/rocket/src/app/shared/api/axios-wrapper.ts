import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { backlink, loggedUserToken } from '../consts';

enum StatusCode {
  BadRequest = 400,
  Unauthorized = 401,
  Forbidden = 403,
  TooManyRequests = 429,
  InternalServerError = 500,
}

const headers: Readonly<Record<string, string | boolean>> = {
  'Access-Control-Allow-Credentials': true,
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
};

// We can use the following function to inject the JWT token through an interceptor
// We get the `accessToken` from the localStorage that we set when we authenticate
const injectToken = (config: AxiosRequestConfig): AxiosRequestConfig => {
  try {
    let token: string | null = null;
    if (typeof window !== 'undefined') {
      token = window.localStorage.getItem(loggedUserToken);
    }
    if (token != null) {
      config!.headers!['Authorization'] = `Bearer ${token}`;
    }

    return config;
  } catch (error: any) {
    throw new Error(error.message);
  }
};

export class Http {
  private instance: AxiosInstance | null = null;

  private get http(): AxiosInstance {
    return this.instance != null ? this.instance : this.initHttp();
  }

  initHttp() {
    const http = axios.create({
      baseURL: backlink,
      headers,
      withCredentials: false,
    });

    http.interceptors.request.use(injectToken, (error) =>
      Promise.reject(error)
    );

    http.interceptors.response.use(
      (response) => response,
      (error) => {
        const { response } = error;
        return this.handleError(response);
      }
    );

    this.instance = http;
    return http;
  }

  get<T = any, R = AxiosResponse<T>>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<R> {
    return this.http.get<T, R>(url, config);
  }

  post<T = any, S = any, R = AxiosResponse<T>>(
    url: string,
    data?: S,
    config?: AxiosRequestConfig
  ): Promise<R> {
    return this.http.post<T, R>(url, data, config);
  }

  put<T = any, R = AxiosResponse<T>>(
    url: string,
    data?: T,
    config?: AxiosRequestConfig
  ): Promise<R> {
    return this.http.put<T, R>(url, data, config);
  }

  delete<T = any, R = AxiosResponse<T>>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<R> {
    return this.http.delete<T, R>(url, config);
  }

  // Handle global app errors
  // We can handle generic app errors depending on the status code
  private handleError(error: any) {
    if (!error.status) return;
    const { status } = error;

    switch (status) {
      case StatusCode.BadRequest: {
        throw Error('Bad request');
      }
      case StatusCode.InternalServerError: {
        throw Error('InternalServerError');
      }
      case StatusCode.Forbidden: {
        throw Error('Forbidden');
      }
      case StatusCode.Unauthorized: {
        throw Error('Unauthorized');
      }
      case StatusCode.TooManyRequests: {
        throw Error('TooManyRequests');
      }
    }
  }
}

export const http = new Http();
