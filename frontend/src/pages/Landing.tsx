import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { useToast } from "@/hooks/use-toast";
import { 
  GraduationCap, 
  Calendar, 
  Lightbulb, 
  Users, 
  BookOpen, 
  Zap,
  ArrowRight,
  Star,
  Award,
  TrendingUp
} from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";
import campusHero from "@/assets/campus-hero.jpg";

export default function Landing() {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleProtectedAction = (action: string, route: string) => {
    if (!isAuthenticated) {
      toast({
        title: "Sign in required",
        description: `Please sign in to ${action}.`,
        variant: "destructive",
      });
      navigate('/login', { state: { from: { pathname: route } } });
    } else {
      navigate(route);
    }
  };
  const features = [
    {
      icon: Calendar,
      title: "Campus Events",
      description: "Discover and join exciting events happening around campus",
      color: "bg-blue-100 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400"
    },
    {
      icon: Lightbulb,
      title: "Project Ideas Hub",
      description: "Share innovative project ideas and collaborate with peers",
      color: "bg-purple-100 text-purple-600 dark:bg-purple-900/20 dark:text-purple-400"
    },
    {
      icon: Users,
      title: "Student Network",
      description: "Connect with like-minded students across departments",
      color: "bg-green-100 text-green-600 dark:bg-green-900/20 dark:text-green-400"
    },
    {
      icon: BookOpen,
      title: "Academic Resources",
      description: "Access study materials and academic support",
      color: "bg-orange-100 text-orange-600 dark:bg-orange-900/20 dark:text-orange-400"
    },
    {
      icon: Award,
      title: "Achievement Badges",
      description: "Earn recognition for your contributions and participation",
      color: "bg-yellow-100 text-yellow-600 dark:bg-yellow-900/20 dark:text-yellow-400"
    },
    {
      icon: TrendingUp,
      title: "Progress Tracking",
      description: "Monitor your academic and social engagement growth",
      color: "bg-pink-100 text-pink-600 dark:bg-pink-900/20 dark:text-pink-400"
    }
  ];

  const stats = [
    { label: "Active Students", value: "2,500+" },
    { label: "Events This Month", value: "45" },
    { label: "Project Ideas", value: "180+" },
    { label: "Success Stories", value: "320+" }
  ];

  return (
    <div className="flex flex-col">
      {/* Hero Section */}
      <section className="relative py-20 md:py-28 overflow-hidden">
        <div className="absolute inset-0 bg-gradient-primary opacity-5"></div>
        <div className="container relative">
          <div className="flex flex-col lg:flex-row items-center gap-12">
            <div className="flex-1 text-center lg:text-left">
              <Badge variant="secondary" className="mb-6 gap-2 animate-pulse">
                <Zap className="h-4 w-4 text-yellow-500" />
                New Platform Launch
              </Badge>
              <h1 className="text-4xl md:text-6xl font-bold mb-6 leading-tight">
                Connect, Learn,{" "}
                <span className="text-gradient">Grow Together</span>
              </h1>
              <p className="text-xl text-muted-foreground mb-8 max-w-2xl">
                Join the ultimate student community platform where ideas flourish, 
                events inspire, and connections last a lifetime. Your campus experience starts here.
              </p>
              <div className="flex flex-col sm:flex-row gap-4 justify-center lg:justify-start">
                <Button 
                  size="lg" 
                  className="bg-gradient-primary hover:opacity-90 hover-scale transition-smooth gap-2 shadow-glow"
                  onClick={() => handleProtectedAction("get started", "/dashboard")}
                >
                  Get Started
                  <ArrowRight className="h-4 w-4" />
                </Button>
                <Button 
                  size="lg" 
                  variant="outline" 
                  className="gap-2 hover-lift transition-smooth"
                  onClick={() => handleProtectedAction("explore events", "/events")}
                >
                  <Calendar className="h-4 w-4" />
                  Explore Events
                </Button>
              </div>
            </div>
            <div className="flex-1 relative">
              <div className="relative w-full aspect-[4/3] max-w-2xl mx-auto">
                <img
                  src={campusHero}
                  alt="Students collaborating on campus with laptops and modern buildings"
                  className="w-full h-full object-cover rounded-3xl shadow-strong"
                />
                <div className="absolute inset-0 bg-gradient-primary/10 rounded-3xl"></div>
                <div className="absolute bottom-6 left-6 right-6">
                  <Card className="backdrop-blur-sm bg-white/90 dark:bg-gray-900/90 border-white/20">
                    <CardContent className="p-4">
                      <div className="grid grid-cols-2 gap-4 text-center">
                        <div>
                          <div className="text-2xl font-bold text-primary">2,500+</div>
                          <div className="text-xs text-muted-foreground">Active Students</div>
                        </div>
                        <div>
                          <div className="text-2xl font-bold text-primary">45</div>
                          <div className="text-xs text-muted-foreground">Events This Month</div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="py-12 bg-muted/30">
        <div className="container">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
            {stats.map((stat, index) => (
              <div key={index} className="text-center">
                <div className="text-3xl md:text-4xl font-bold text-gradient mb-2">
                  {stat.value}
                </div>
                <div className="text-sm text-muted-foreground">
                  {stat.label}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20">
        <div className="container">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold mb-4">
              Everything You Need to <span className="text-gradient">Succeed</span>
            </h2>
            <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
              Discover powerful features designed to enhance your campus experience and help you make the most of your academic journey.
            </p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <Card key={index} className="group hover-lift border-border/50 hover:border-primary/20 transition-smooth">
                <CardHeader>
                  <div className={`inline-flex p-3 rounded-lg ${feature.color} w-fit mb-4`}>
                    <feature.icon className="h-6 w-6" />
                  </div>
                  <CardTitle className="group-hover:text-gradient transition-smooth">
                    {feature.title}
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <p className="text-muted-foreground">
                    {feature.description}
                  </p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-gradient-primary relative overflow-hidden">
        <div className="absolute inset-0 bg-grid-white/[0.05] bg-grid-16"></div>
        <div className="container relative text-center">
          <h2 className="text-3xl md:text-5xl font-bold text-white mb-6">
            Ready to Join the Community?
          </h2>
          <p className="text-xl text-white/90 mb-8 max-w-2xl mx-auto">
            Start your journey today and become part of something bigger. Connect with thousands of students already making a difference.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button size="lg" variant="secondary" className="gap-2 hover-scale" asChild>
              <Link to="/signup">
                <GraduationCap className="h-4 w-4" />
                Create Account
              </Link>
            </Button>
            <Button 
              size="lg" 
              variant="outline" 
              className="gap-2 border-white/20 text-white hover:bg-white/10"
              onClick={() => handleProtectedAction("learn more", "/dashboard")}
            >
              Learn More
            </Button>
          </div>
        </div>
      </section>
    </div>
  );
}