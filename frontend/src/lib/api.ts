// API Configuration for Campus Connect Frontend

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api/v1';

// API Client Configuration
class ApiClient {
  private baseURL: string;
  private token: string | null = null;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
    this.token = localStorage.getItem('campus-connect-token');
  }

  private getHeaders(): HeadersInit {
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
    };

    if (this.token) {
      headers.Authorization = `Bearer ${this.token}`;
    }

    return headers;
  }

  setToken(token: string) {
    this.token = token;
    localStorage.setItem('campus-connect-token', token);
  }

  clearToken() {
    this.token = null;
    localStorage.removeItem('campus-connect-token');
  }

  async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${this.baseURL}${endpoint}`;
    
    const config: RequestInit = {
      ...options,
      headers: {
        ...this.getHeaders(),
        ...options.headers,
      },
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
      }

      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // GET request
  async get<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'GET' });
  }

  // POST request
  async post<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  // PUT request
  async put<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  // DELETE request
  async delete<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'DELETE' });
  }
}

// Create API client instance
export const apiClient = new ApiClient(API_BASE_URL);

// API Endpoints
export const API_ENDPOINTS = {
  // Authentication
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    REFRESH: '/auth/refresh',
    LOGOUT: '/auth/logout',
  },
  
  // Users
  USERS: {
    PROFILE: '/users/profile',
    UPDATE_PROFILE: '/users/profile',
    LIST: '/users',
    BY_ID: (id: string) => `/users/${id}`,
  },
  
  // Events
  EVENTS: {
    LIST: '/events',
    CREATE: '/events',
    BY_ID: (id: string) => `/events/${id}`,
    UPDATE: (id: string) => `/events/${id}`,
    DELETE: (id: string) => `/events/${id}`,
    REGISTER: (id: string) => `/events/${id}/register`,
    UNREGISTER: (id: string) => `/events/${id}/unregister`,
  },
  
  // Projects
  PROJECTS: {
    LIST: '/projects',
    CREATE: '/projects',
    BY_ID: (id: string) => `/projects/${id}`,
    UPDATE: (id: string) => `/projects/${id}`,
    DELETE: (id: string) => `/projects/${id}`,
    LIKE: (id: string) => `/projects/${id}/like`,
    UNLIKE: (id: string) => `/projects/${id}/unlike`,
    COMMENTS: (id: string) => `/projects/${id}/comments`,
    ADD_COMMENT: (id: string) => `/projects/${id}/comments`,
    COLLABORATORS: (id: string) => `/projects/${id}/collaborators`,
    ADD_COLLABORATOR: (id: string) => `/projects/${id}/collaborators`,
  },
  
  // Health Check
  HEALTH: '/actuator/health',
} as const;

// Type definitions for API responses
export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  user: {
    id: string;
    username: string;
    email: string;
    firstName: string;
    lastName: string;
    role: string;
  };
}

export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
}

// API Service Functions
export const authService = {
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>(API_ENDPOINTS.AUTH.LOGIN, credentials);
    apiClient.setToken(response.token);
    return response;
  },

  register: async (userData: RegisterRequest): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>(API_ENDPOINTS.AUTH.REGISTER, userData);
    apiClient.setToken(response.token);
    return response;
  },

  logout: () => {
    apiClient.clearToken();
  },
};

export const eventService = {
  getAll: () => apiClient.get(API_ENDPOINTS.EVENTS.LIST),
  getById: (id: string) => apiClient.get(API_ENDPOINTS.EVENTS.BY_ID(id)),
  create: (eventData: any) => apiClient.post(API_ENDPOINTS.EVENTS.CREATE, eventData),
  update: (id: string, eventData: any) => apiClient.put(API_ENDPOINTS.EVENTS.UPDATE(id), eventData),
  delete: (id: string) => apiClient.delete(API_ENDPOINTS.EVENTS.DELETE(id)),
  register: (id: string) => apiClient.post(API_ENDPOINTS.EVENTS.REGISTER(id)),
  unregister: (id: string) => apiClient.delete(API_ENDPOINTS.EVENTS.UNREGISTER(id)),
};

export const projectService = {
  getAll: () => apiClient.get(API_ENDPOINTS.PROJECTS.LIST),
  getById: (id: string) => apiClient.get(API_ENDPOINTS.PROJECTS.BY_ID(id)),
  create: (projectData: any) => apiClient.post(API_ENDPOINTS.PROJECTS.CREATE, projectData),
  update: (id: string, projectData: any) => apiClient.put(API_ENDPOINTS.PROJECTS.UPDATE(id), projectData),
  delete: (id: string) => apiClient.delete(API_ENDPOINTS.PROJECTS.DELETE(id)),
  like: (id: string) => apiClient.post(API_ENDPOINTS.PROJECTS.LIKE(id)),
  unlike: (id: string) => apiClient.delete(API_ENDPOINTS.PROJECTS.UNLIKE(id)),
  getComments: (id: string) => apiClient.get(API_ENDPOINTS.PROJECTS.COMMENTS(id)),
  addComment: (id: string, comment: any) => apiClient.post(API_ENDPOINTS.PROJECTS.ADD_COMMENT(id), comment),
};

export const userService = {
  getProfile: () => apiClient.get(API_ENDPOINTS.USERS.PROFILE),
  updateProfile: (userData: any) => apiClient.put(API_ENDPOINTS.USERS.UPDATE_PROFILE, userData),
  getById: (id: string) => apiClient.get(API_ENDPOINTS.USERS.BY_ID(id)),
};

// Health check function
export const healthCheck = () => apiClient.get(API_ENDPOINTS.HEALTH);

export default apiClient;