import { useLocation, Link } from "react-router-dom";
import { useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Home, ArrowLeft, Search, GraduationCap } from "lucide-react";

const NotFound = () => {
  const location = useLocation();

  useEffect(() => {
    console.error("404 Error: User attempted to access non-existent route:", location.pathname);
  }, [location.pathname]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-secondary p-4">
      <Card className="max-w-md w-full shadow-strong">
        <CardContent className="p-8 text-center">
          <div className="mb-6">
            <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-primary mx-auto mb-4">
              <GraduationCap className="h-8 w-8 text-white" />
            </div>
            <h1 className="text-6xl font-bold text-gradient mb-2">404</h1>
            <h2 className="text-2xl font-semibold mb-2">Page Not Found</h2>
            <p className="text-muted-foreground mb-6">
              Oops! The page you're looking for doesn't exist. It might have been moved, deleted, or you entered the wrong URL.
            </p>
          </div>

          <div className="space-y-3">
            <Button asChild className="w-full bg-gradient-primary gap-2">
              <Link to="/">
                <Home className="h-4 w-4" />
                Back to Home
              </Link>
            </Button>
            <Button variant="outline" asChild className="w-full gap-2">
              <Link to="/dashboard">
                <ArrowLeft className="h-4 w-4" />
                Go to Dashboard
              </Link>
            </Button>
            <Button variant="ghost" asChild className="w-full gap-2">
              <Link to="/events">
                <Search className="h-4 w-4" />
                Browse Events
              </Link>
            </Button>
          </div>

          <div className="mt-6 pt-6 border-t border-border">
            <p className="text-sm text-muted-foreground">
              Need help? Contact our support team or visit our help center.
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default NotFound;
