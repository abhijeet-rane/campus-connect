import { useState, useEffect } from "react";
import { Outlet, useLocation, Link, Navigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuSeparator, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { useToast } from "@/hooks/use-toast";
import { 
  Menu, 
  Home, 
  Calendar, 
  Lightbulb, 
  User, 
  LogIn,
  LogOut,
  Moon,
  Sun,
  GraduationCap,
  Settings,
  Bell
} from "lucide-react";
import { cn } from "@/lib/utils";
import { useAuth } from "@/contexts/AuthContext";

const navigation = [
  { name: "Home", href: "/", icon: Home },
  { name: "Dashboard", href: "/dashboard", icon: GraduationCap },
  { name: "Events", href: "/events", icon: Calendar },
  { name: "Project Ideas", href: "/projects", icon: Lightbulb },
  { name: "Profile", href: "/profile", icon: User },
];

export default function Layout() {
  const [isDark, setIsDark] = useState(() => {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('theme') === 'dark' || 
             (!localStorage.getItem('theme') && window.matchMedia('(prefers-color-scheme: dark)').matches);
    }
    return false;
  });
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation();
  const { user, logout, isAuthenticated } = useAuth();
  const { toast } = useToast();

  useEffect(() => {
    if (isDark) {
      document.documentElement.classList.add("dark");
      localStorage.setItem('theme', 'dark');
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem('theme', 'light');
    }
  }, [isDark]);

  const toggleTheme = () => {
    setIsDark(!isDark);
  };

  const handleLogout = () => {
    logout();
    toast({
      title: "Signed out successfully",
      description: "You have been logged out of your account.",
    });
  };

  const isActive = (href: string) => location.pathname === href;

  // Redirect unauthenticated users trying to access protected routes
  if (!isAuthenticated && location.pathname !== '/' && !location.pathname.startsWith('/login') && !location.pathname.startsWith('/signup')) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return (
    <div className={cn("min-h-screen bg-background", isDark && "dark")}>
      {/* Header */}
      <header className="sticky top-0 z-50 w-full border-b border-border/40 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
        <div className="container flex h-16 items-center">
          {/* Mobile menu */}
          <Sheet open={isOpen} onOpenChange={setIsOpen}>
            <SheetTrigger asChild className="md:hidden">
              <Button variant="ghost" size="icon">
                <Menu className="h-5 w-5" />
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="w-72">
              <div className="flex items-center gap-2 mb-8">
                <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-primary">
                  <GraduationCap className="h-5 w-5 text-white" />
                </div>
                <span className="text-xl font-bold text-gradient">Campus Connect</span>
              </div>
              <nav className="grid gap-2">
                {navigation.map((item) => (
                  <Button
                    key={item.name}
                    variant={isActive(item.href) ? "secondary" : "ghost"}
                    className="justify-start gap-3"
                    onClick={() => setIsOpen(false)}
                    asChild
                  >
                    <Link to={item.href}>
                      <item.icon className="h-4 w-4" />
                      {item.name}
                    </Link>
                  </Button>
                ))}
              </nav>
            </SheetContent>
          </Sheet>

          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 mr-6">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-primary">
              <GraduationCap className="h-5 w-5 text-white" />
            </div>
            <span className="text-xl font-bold text-gradient hidden sm:inline-block">
              Campus Connect
            </span>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center space-x-1 flex-1">
            {navigation.map((item) => (
              <Button
                key={item.name}
                variant={isActive(item.href) ? "secondary" : "ghost"}
                className="gap-2"
                asChild
              >
                <Link to={item.href}>
                  <item.icon className="h-4 w-4" />
                  {item.name}
                </Link>
              </Button>
            ))}
          </nav>

          <div className="flex items-center gap-2 ml-auto">
            {/* Theme Toggle */}
            <Button variant="ghost" size="icon" onClick={toggleTheme}>
              {isDark ? (
                <Sun className="h-4 w-4" />
              ) : (
                <Moon className="h-4 w-4" />
              )}
            </Button>

            {isAuthenticated ? (
              <>
                {/* Notifications */}
                <Button variant="ghost" size="icon" className="relative">
                  <Bell className="h-4 w-4" />
                  <span className="absolute -top-1 -right-1 h-3 w-3 bg-destructive rounded-full text-xs flex items-center justify-center text-white">
                    2
                  </span>
                </Button>

                {/* User Menu */}
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" className="flex items-center gap-2 pl-2 pr-3 hover-lift">
                      <Avatar className="h-7 w-7 ring-2 ring-primary/20">
                        <AvatarImage src={user?.avatar} alt={user?.name} />
                        <AvatarFallback className="text-xs bg-gradient-primary text-white">
                          {user?.name?.split(' ').map(n => n[0]).join('').toUpperCase()}
                        </AvatarFallback>
                      </Avatar>
                      <span className="hidden sm:inline text-sm font-medium">{user?.name}</span>
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end" className="w-72 shadow-strong">
                    <div className="px-3 py-3 bg-gradient-primary/5 border-b">
                      <div className="flex items-center gap-3">
                        <Avatar className="h-12 w-12">
                          <AvatarImage src={user?.avatar} alt={user?.name} />
                          <AvatarFallback className="bg-gradient-primary text-white">
                            {user?.name?.split(' ').map(n => n[0]).join('').toUpperCase()}
                          </AvatarFallback>
                        </Avatar>
                        <div className="flex-1">
                          <p className="font-medium">{user?.name}</p>
                          <p className="text-sm text-muted-foreground">{user?.email}</p>
                          <div className="flex items-center gap-2 mt-1">
                            <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded-full capitalize">
                              {user?.role}
                            </span>
                            {user?.department && (
                              <span className="text-xs text-muted-foreground">
                                {user.department}
                              </span>
                            )}
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="p-2">
                      <DropdownMenuItem asChild>
                        <Link to="/profile" className="flex items-center gap-3 p-2 rounded-md">
                          <User className="h-4 w-4" />
                          <div>
                            <p className="font-medium">View Profile</p>
                            <p className="text-xs text-muted-foreground">Manage your account</p>
                          </div>
                        </Link>
                      </DropdownMenuItem>
                      <DropdownMenuItem>
                        <Settings className="h-4 w-4 mr-3" />
                        <div>
                          <p className="font-medium">Settings</p>
                          <p className="text-xs text-muted-foreground">Preferences & privacy</p>
                        </div>
                      </DropdownMenuItem>
                      <DropdownMenuSeparator />
                      <DropdownMenuItem onClick={handleLogout} className="text-destructive focus:text-destructive">
                        <LogOut className="h-4 w-4 mr-3" />
                        <div>
                          <p className="font-medium">Sign Out</p>
                          <p className="text-xs text-muted-foreground">Sign out of your account</p>
                        </div>
                      </DropdownMenuItem>
                    </div>
                  </DropdownMenuContent>
                </DropdownMenu>
              </>
            ) : (
              <>
                {/* Auth Buttons */}
                <Button variant="ghost" className="gap-2" asChild>
                  <Link to="/login">
                    <LogIn className="h-4 w-4" />
                    <span className="hidden sm:inline">Sign In</span>
                  </Link>
                </Button>
                <Button className="gap-2 bg-gradient-primary hover:opacity-90 transition-fast" asChild>
                  <Link to="/signup">
                    <User className="h-4 w-4" />
                    <span className="hidden sm:inline">Sign Up</span>
                  </Link>
                </Button>
              </>
            )}
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1">
        <Outlet />
      </main>

      {/* Footer */}
      <footer className="border-t border-border/40 bg-muted/30">
        <div className="container py-8">
          <div className="flex flex-col md:flex-row justify-between items-center gap-4">
            <div className="flex items-center gap-2">
              <div className="flex h-6 w-6 items-center justify-center rounded-md bg-gradient-primary">
                <GraduationCap className="h-4 w-4 text-white" />
              </div>
              <span className="font-semibold text-gradient">Campus Connect</span>
            </div>
            <p className="text-sm text-muted-foreground">
              © 2024 Campus Connect. Empowering student communities.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
}