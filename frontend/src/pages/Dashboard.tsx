import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Progress } from "@/components/ui/progress";
import { Link } from "react-router-dom";
import { 
  Calendar, 
  Lightbulb, 
  Users, 
  Award, 
  Clock,
  TrendingUp,
  BookOpen,
  Bell,
  Plus,
  ArrowRight,
  Star,
  Target
} from "lucide-react";

export default function Dashboard() {
  const recentEvents = [
    {
      id: 1,
      title: "Tech Innovation Summit 2024",
      date: "Nov 25, 2024",
      time: "2:00 PM",
      attendees: 124,
      status: "registered"
    },
    {
      id: 2,
      title: "Startup Pitch Competition",
      date: "Nov 28, 2024", 
      time: "10:00 AM",
      attendees: 89,
      status: "interested"
    },
    {
      id: 3,
      title: "Career Fair - Engineering",
      date: "Dec 2, 2024",
      time: "9:00 AM",
      attendees: 256,
      status: "registered"
    }
  ];

  const recentProjects = [
    {
      id: 1,
      title: "AI-Powered Study Assistant",
      author: "Sarah Chen",
      likes: 24,
      comments: 8,
      tags: ["AI", "Education", "Mobile App"]
    },
    {
      id: 2,
      title: "Campus Sustainability Tracker",
      author: "Mike Johnson",
      likes: 31,
      comments: 12,
      tags: ["Environment", "IoT", "Data Analytics"]
    },
    {
      id: 3,
      title: "Virtual Reality Lab Experience",
      author: "Alex Rodriguez",
      likes: 18,
      comments: 6,
      tags: ["VR", "Education", "3D Modeling"]
    }
  ];

  const achievements = [
    { name: "Event Explorer", description: "Attended 5 events", progress: 100, icon: Calendar },
    { name: "Idea Generator", description: "Posted 3 project ideas", progress: 60, icon: Lightbulb },
    { name: "Community Builder", description: "Connected with 10 students", progress: 80, icon: Users },
    { name: "Active Contributor", description: "Weekly engagement streak", progress: 45, icon: Target }
  ];

  const quickStats = [
    { label: "Events Attended", value: "12", icon: Calendar, color: "text-blue-600" },
    { label: "Ideas Shared", value: "5", icon: Lightbulb, color: "text-purple-600" },
    { label: "Connections", value: "28", icon: Users, color: "text-green-600" },
    { label: "Badges Earned", value: "8", icon: Award, color: "text-orange-600" }
  ];

  return (
    <div className="container py-8">
      {/* Welcome Section */}
      <div className="mb-8">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold mb-2">
              Welcome back, <span className="text-gradient">Alex!</span>
            </h1>
            <p className="text-muted-foreground">
              Here's what's happening in your campus community today.
            </p>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" size="sm" className="gap-2">
              <Bell className="h-4 w-4" />
              Notifications
            </Button>
            <Button size="sm" className="bg-gradient-primary gap-2" asChild>
              <Link to="/projects/create">
                <Plus className="h-4 w-4" />
                New Idea
              </Link>
            </Button>
          </div>
        </div>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        {quickStats.map((stat, index) => (
          <Card key={index} className="hover-lift">
            <CardContent className="p-6">
              <div className="flex items-center gap-3">
                <div className={`p-2 bg-muted rounded-lg ${stat.color}`}>
                  <stat.icon className="h-4 w-4" />
                </div>
                <div>
                  <p className="text-2xl font-bold">{stat.value}</p>
                  <p className="text-sm text-muted-foreground">{stat.label}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-8">
          {/* Recent Events */}
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Calendar className="h-5 w-5" />
                Upcoming Events
              </CardTitle>
              <Button variant="ghost" size="sm" className="gap-2" asChild>
                <Link to="/events">
                  View All
                  <ArrowRight className="h-4 w-4" />
                </Link>
              </Button>
            </CardHeader>
            <CardContent className="space-y-4">
              {recentEvents.map((event) => (
                <div key={event.id} className="flex items-center justify-between p-4 border border-border rounded-lg hover-lift transition-smooth">
                  <div className="flex-1">
                    <h3 className="font-medium mb-1">{event.title}</h3>
                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                      <span className="flex items-center gap-1">
                        <Calendar className="h-3 w-3" />
                        {event.date}
                      </span>
                      <span className="flex items-center gap-1">
                        <Clock className="h-3 w-3" />
                        {event.time}
                      </span>
                      <span className="flex items-center gap-1">
                        <Users className="h-3 w-3" />
                        {event.attendees} attending
                      </span>
                    </div>
                  </div>
                  <Badge variant={event.status === 'registered' ? 'default' : 'secondary'}>
                    {event.status}
                  </Badge>
                </div>
              ))}
            </CardContent>
          </Card>

          {/* Recent Project Ideas */}
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Lightbulb className="h-5 w-5" />
                Trending Project Ideas
              </CardTitle>
              <Button variant="ghost" size="sm" className="gap-2" asChild>
                <Link to="/projects">
                  Explore More
                  <ArrowRight className="h-4 w-4" />
                </Link>
              </Button>
            </CardHeader>
            <CardContent className="space-y-4">
              {recentProjects.map((project) => (
                <div key={project.id} className="p-4 border border-border rounded-lg hover-lift transition-smooth">
                  <h3 className="font-medium mb-2">{project.title}</h3>
                  <div className="flex items-center gap-2 mb-3">
                    <Avatar className="h-6 w-6">
                      <AvatarImage src="" />
                      <AvatarFallback className="text-xs">
                        {project.author.split(' ').map(n => n[0]).join('')}
                      </AvatarFallback>
                    </Avatar>
                    <span className="text-sm text-muted-foreground">by {project.author}</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex gap-1">
                      {project.tags.map((tag, index) => (
                        <Badge key={index} variant="secondary" className="text-xs">
                          {tag}
                        </Badge>
                      ))}
                    </div>
                    <div className="flex items-center gap-3 text-sm text-muted-foreground">
                      <span className="flex items-center gap-1">
                        <Star className="h-3 w-3" />
                        {project.likes}
                      </span>
                      <span>{project.comments} comments</span>
                    </div>
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Achievement Progress */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Award className="h-5 w-5" />
                Your Achievements
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {achievements.map((achievement, index) => (
                <div key={index} className="space-y-2">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <achievement.icon className="h-4 w-4 text-primary" />
                      <span className="font-medium text-sm">{achievement.name}</span>
                    </div>
                    <span className="text-xs text-muted-foreground">{achievement.progress}%</span>
                  </div>
                  <Progress value={achievement.progress} className="h-2" />
                  <p className="text-xs text-muted-foreground">{achievement.description}</p>
                </div>
              ))}
            </CardContent>
          </Card>

          {/* Quick Actions */}
          <Card>
            <CardHeader>
              <CardTitle>Quick Actions</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              <Button variant="outline" className="w-full justify-start gap-2" asChild>
                <Link to="/events">
                  <Calendar className="h-4 w-4" />
                  Browse Events
                </Link>
              </Button>
              <Button variant="outline" className="w-full justify-start gap-2" asChild>
                <Link to="/projects/create">
                  <Lightbulb className="h-4 w-4" />
                  Share Project Idea
                </Link>
              </Button>
              <Button variant="outline" className="w-full justify-start gap-2" asChild>
                <Link to="/projects">
                  <Users className="h-4 w-4" />
                  Find Study Partners
                </Link>
              </Button>
              <Button variant="outline" className="w-full justify-start gap-2">
                <BookOpen className="h-4 w-4" />
                Study Resources
              </Button>
            </CardContent>
          </Card>

          {/* Activity Feed */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="h-5 w-5" />
                Recent Activity
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              <div className="text-sm space-y-2">
                <p className="flex items-center gap-2">
                  <div className="h-2 w-2 bg-green-500 rounded-full"></div>
                  You joined "Tech Summit 2024"
                </p>
                <p className="flex items-center gap-2">
                  <div className="h-2 w-2 bg-blue-500 rounded-full"></div>
                  New comment on your project idea
                </p>
                <p className="flex items-center gap-2">
                  <div className="h-2 w-2 bg-purple-500 rounded-full"></div>
                  Sarah liked your project
                </p>
                <p className="flex items-center gap-2">
                  <div className="h-2 w-2 bg-orange-500 rounded-full"></div>
                  Earned "Event Explorer" badge
                </p>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}