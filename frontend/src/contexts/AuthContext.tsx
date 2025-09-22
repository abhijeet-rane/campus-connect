import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { authService, LoginRequest, LoginResponse } from '../lib/api';

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'student' | 'admin';
  department?: string;
  year?: string;
  avatar?: string;
  username?: string;
  firstName?: string;
  lastName?: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check for existing session
    const savedUser = localStorage.getItem('campus-connect-user');
    const token = localStorage.getItem('campus-connect-token');
    
    if (savedUser && token) {
      setUser(JSON.parse(savedUser));
    }
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    setIsLoading(true);
    
    try {
      const credentials: LoginRequest = { email, password };
      const response: LoginResponse = await authService.login(credentials);
      
      const userData: User = {
        id: response.user.id,
        name: `${response.user.firstName} ${response.user.lastName}`,
        email: response.user.email,
        role: response.user.role === 'ADMIN' ? 'admin' : 'student',
        username: response.user.username,
        firstName: response.user.firstName,
        lastName: response.user.lastName,
        avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${response.user.email}`
      };
      
      setUser(userData);
      localStorage.setItem('campus-connect-user', JSON.stringify(userData));
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
    localStorage.removeItem('campus-connect-user');
  };

  return (
    <AuthContext.Provider value={{
      user,
      login,
      logout,
      isAuthenticated: !!user,
      isLoading
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}