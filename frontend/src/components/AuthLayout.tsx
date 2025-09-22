import { GraduationCap, BookOpen, Users, Award, Star } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Link } from 'react-router-dom';

interface AuthLayoutProps {
  children: React.ReactNode;
  title: string;
  subtitle: string;
}

export default function AuthLayout({ children, title, subtitle }: AuthLayoutProps) {
  const features = [
    { icon: BookOpen, text: "Access to exclusive academic resources" },
    { icon: Users, text: "Connect with 2,500+ active students" },
    { icon: Award, text: "Earn recognition through gamified badges" },
    { icon: Star, text: "Join 45+ events happening this month" }
  ];

  return (
    <div className="min-h-screen flex">
      {/* Left side - Branding & Features */}
      <div className="hidden lg:flex lg:flex-1 bg-gradient-primary relative overflow-hidden">
        <div className="absolute inset-0 bg-grid-white/[0.05] bg-grid-16"></div>
        <div className="relative flex flex-col justify-center px-12 py-16 text-white">
          <div className="max-w-lg">
            {/* Logo */}
            <div className="flex items-center gap-3 mb-8">
              <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-white/20 backdrop-blur-sm">
                <GraduationCap className="h-7 w-7 text-white" />
              </div>
              <span className="text-2xl font-bold">Campus Connect</span>
            </div>

            {/* Welcome Message */}
            <h1 className="text-4xl font-bold mb-4 leading-tight">
              Welcome to Your Campus Community
            </h1>
            <p className="text-white/90 text-lg mb-8 leading-relaxed">
              Join thousands of students connecting, learning, and growing together. 
              Your academic journey starts here.
            </p>

            {/* Features List */}
            <div className="space-y-4 mb-8">
              {features.map((feature, index) => (
                <div key={index} className="flex items-center gap-3">
                  <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-white/20 backdrop-blur-sm">
                    <feature.icon className="h-4 w-4 text-white" />
                  </div>
                  <span className="text-white/90">{feature.text}</span>
                </div>
              ))}
            </div>

            {/* Back to Home */}
            <Button 
              variant="outline" 
              className="border-white/20 text-white hover:bg-white/10 gap-2"
              asChild
            >
              <Link to="/">
                ← Back to Home
              </Link>
            </Button>
          </div>
        </div>
      </div>

      {/* Right side - Auth Form */}
      <div className="flex-1 lg:flex-none lg:w-[480px] flex items-center justify-center p-8">
        <div className="w-full max-w-md">
          {/* Mobile Logo */}
          <div className="lg:hidden flex items-center justify-center gap-2 mb-8">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-primary">
              <GraduationCap className="h-6 w-6 text-white" />
            </div>
            <span className="text-xl font-bold text-gradient">Campus Connect</span>
          </div>

          {/* Form Header */}
          <div className="text-center mb-8">
            <h2 className="text-3xl font-bold mb-2">{title}</h2>
            <p className="text-muted-foreground">{subtitle}</p>
          </div>

          {/* Form Content */}
          {children}
        </div>
      </div>
    </div>
  );
}