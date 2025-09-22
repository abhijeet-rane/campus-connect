import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Progress } from "@/components/ui/progress";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Link } from "react-router-dom";
import { 
  User, 
  Edit,
  MapPin,
  Calendar,
  Mail,
  Github,
  Linkedin,
  Globe,
  Award,
  Lightbulb,
  Users,
  BookOpen,
  TrendingUp,
  Star,
  Heart,
  MessageCircle,
  Eye,
  Clock,
  Target,
  Zap
} from "lucide-react";

export default function Profile() {
  const user = {
    name: "Alex Rodriguez",
    email: "alex.rodriguez@university.edu",
    year: "Computer Science, 4th Year",
    location: "Campus Dorms, Building C",
    joinDate: "September 2021",
    bio: "Passionate computer science student with interests in AI, web development, and creating innovative solutions for campus life. Always excited to collaborate on new projects!",
    avatar: "",
    social: {
      github: "alexrodriguez",
      linkedin: "alex-rodriguez-cs",
      website: "alexrodriguez.dev"
    },
    stats: {
      eventsAttended: 24,
      projectsShared: 8,
      collaborations: 15,
      badgesEarned: 12
    }
  };

  const badges = [
    {
      name: "Event Explorer",
      description: "Attended 20+ campus events",
      icon: Calendar,
      progress: 100,
      color: "bg-blue-100 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400",
      earned: true,
      earnedDate: "Oct 15, 2024"
    },
    {
      name: "Idea Generator",
      description: "Shared 5+ project ideas",
      icon: Lightbulb,
      progress: 100,
      color: "bg-yellow-100 text-yellow-600 dark:bg-yellow-900/20 dark:text-yellow-400",
      earned: true,
      earnedDate: "Sep 22, 2024"
    },
    {
      name: "Community Builder",
      description: "Connected with 25+ students",
      icon: Users,
      progress: 80,
      color: "bg-green-100 text-green-600 dark:bg-green-900/20 dark:text-green-400",
      earned: false,
      earnedDate: null
    },
    {
      name: "Active Contributor",
      description: "Maintain 30-day engagement streak",
      icon: Target,
      progress: 60,
      color: "bg-purple-100 text-purple-600 dark:bg-purple-900/20 dark:text-purple-400",
      earned: false,
      earnedDate: null
    },
    {
      name: "Tech Innovator",
      description: "Lead 3+ successful projects",
      icon: Zap,
      progress: 33,
      color: "bg-orange-100 text-orange-600 dark:bg-orange-900/20 dark:text-orange-400",
      earned: false,
      earnedDate: null
    },
    {
      name: "Mentor",
      description: "Help 10+ students with projects",
      icon: BookOpen,
      progress: 70,
      color: "bg-pink-100 text-pink-600 dark:bg-pink-900/20 dark:text-pink-400",
      earned: false,
      earnedDate: null
    }
  ];

  const recentProjects = [
    {
      id: 1,
      title: "Virtual Reality Lab Experience",
      description: "Create immersive VR experiences for chemistry and physics labs",
      status: "completed",
      likes: 42,
      comments: 15,
      views: 298,
      tags: ["VR", "Education", "3D Modeling"]
    },
    {
      id: 2,
      title: "Campus Navigation AR App",
      description: "Augmented reality indoor navigation for campus buildings",
      status: "in-development",
      likes: 28,
      comments: 8,
      views: 187,
      tags: ["AR", "Mobile", "Navigation"]
    },
    {
      id: 3,
      title: "AI Study Group Matcher",
      description: "Machine learning algorithm to match students for study groups",
      status: "seeking-collaborators",
      likes: 35,
      comments: 12,
      views: 234,
      tags: ["AI", "Social", "Algorithm"]
    }
  ];

  const recentActivity = [
    {
      type: "project",
      action: "Published a new project idea",
      title: "Smart Campus Energy Monitor",
      timeAgo: "2 hours ago"
    },
    {
      type: "event",
      action: "Registered for event",
      title: "Tech Innovation Summit 2024",
      timeAgo: "1 day ago"
    },
    {
      type: "collaboration",
      action: "Joined collaboration on",
      title: "Student Expense Analytics Platform",
      timeAgo: "3 days ago"
    },
    {
      type: "achievement",
      action: "Earned badge",
      title: "Event Explorer",
      timeAgo: "1 week ago"
    }
  ];

  const getStatusColor = (status: string) => {
    switch (status) {
      case "completed": return "bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400";
      case "in-development": return "bg-yellow-100 text-yellow-700 dark:bg-yellow-900/20 dark:text-yellow-400";
      case "seeking-collaborators": return "bg-blue-100 text-blue-700 dark:bg-blue-900/20 dark:text-blue-400";
      default: return "bg-gray-100 text-gray-700 dark:bg-gray-900/20 dark:text-gray-400";
    }
  };

  return (
    <div className="container py-8">
      {/* Profile Header */}
      <Card className="mb-8">
        <CardContent className="p-8">
          <div className="flex flex-col lg:flex-row gap-8">
            <div className="flex flex-col sm:flex-row items-center sm:items-start gap-6">
              <Avatar className="h-32 w-32">
                <AvatarImage src={user.avatar} />
                <AvatarFallback className="text-2xl">
                  {user.name.split(' ').map(n => n[0]).join('')}
                </AvatarFallback>
              </Avatar>
              <div className="text-center sm:text-left">
                <h1 className="text-3xl font-bold mb-2">{user.name}</h1>
                <p className="text-lg text-muted-foreground mb-3">{user.year}</p>
                <div className="space-y-2 text-sm text-muted-foreground">
                  <div className="flex items-center gap-2 justify-center sm:justify-start">
                    <Mail className="h-4 w-4" />
                    {user.email}
                  </div>
                  <div className="flex items-center gap-2 justify-center sm:justify-start">
                    <MapPin className="h-4 w-4" />
                    {user.location}
                  </div>
                  <div className="flex items-center gap-2 justify-center sm:justify-start">
                    <Calendar className="h-4 w-4" />
                    Joined {user.joinDate}
                  </div>
                </div>
              </div>
            </div>

            <div className="flex-1">
              <div className="flex justify-between items-start mb-4">
                <h2 className="text-lg font-semibold">About</h2>
                <Button variant="outline" size="sm" className="gap-2">
                  <Edit className="h-4 w-4" />
                  Edit Profile
                </Button>
              </div>
              <p className="text-muted-foreground mb-6">{user.bio}</p>
              
              {/* Social Links */}
              <div className="flex gap-3 mb-6">
                <Button variant="outline" size="sm" className="gap-2">
                  <Github className="h-4 w-4" />
                  {user.social.github}
                </Button>
                <Button variant="outline" size="sm" className="gap-2">
                  <Linkedin className="h-4 w-4" />
                  LinkedIn
                </Button>
                <Button variant="outline" size="sm" className="gap-2">
                  <Globe className="h-4 w-4" />
                  Website
                </Button>
              </div>

              {/* Quick Stats */}
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div className="text-center p-3 bg-muted/30 rounded-lg">
                  <div className="text-2xl font-bold text-primary">{user.stats.eventsAttended}</div>
                  <div className="text-sm text-muted-foreground">Events</div>
                </div>
                <div className="text-center p-3 bg-muted/30 rounded-lg">
                  <div className="text-2xl font-bold text-primary">{user.stats.projectsShared}</div>
                  <div className="text-sm text-muted-foreground">Projects</div>
                </div>
                <div className="text-center p-3 bg-muted/30 rounded-lg">
                  <div className="text-2xl font-bold text-primary">{user.stats.collaborations}</div>
                  <div className="text-sm text-muted-foreground">Collaborations</div>
                </div>
                <div className="text-center p-3 bg-muted/30 rounded-lg">
                  <div className="text-2xl font-bold text-primary">{user.stats.badgesEarned}</div>
                  <div className="text-sm text-muted-foreground">Badges</div>
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2">
          <Tabs defaultValue="projects" className="space-y-6">
            <TabsList className="grid w-full grid-cols-3">
              <TabsTrigger value="projects">Projects</TabsTrigger>
              <TabsTrigger value="activity">Activity</TabsTrigger>
              <TabsTrigger value="achievements">Achievements</TabsTrigger>
            </TabsList>

            <TabsContent value="projects" className="space-y-6">
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Lightbulb className="h-5 w-5" />
                    My Project Ideas
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  {recentProjects.map((project) => (
                    <div key={project.id} className="border border-border rounded-lg p-4 hover-lift transition-smooth">
                      <div className="flex items-start justify-between mb-3">
                        <div className="flex-1">
                          <h3 className="font-medium mb-1">{project.title}</h3>
                          <p className="text-sm text-muted-foreground mb-2">
                            {project.description}
                          </p>
                        </div>
                        <Badge className={getStatusColor(project.status)} variant="secondary">
                          {project.status.replace('-', ' ')}
                        </Badge>
                      </div>
                      
                      <div className="flex gap-1 flex-wrap mb-3">
                        {project.tags.map((tag, index) => (
                          <Badge key={index} variant="outline" className="text-xs">
                            {tag}
                          </Badge>
                        ))}
                      </div>

                      <div className="flex items-center justify-between text-sm text-muted-foreground">
                        <div className="flex items-center gap-4">
                          <span className="flex items-center gap-1">
                            <Heart className="h-3 w-3" />
                            {project.likes}
                          </span>
                          <span className="flex items-center gap-1">
                            <MessageCircle className="h-3 w-3" />
                            {project.comments}
                          </span>
                          <span className="flex items-center gap-1">
                            <Eye className="h-3 w-3" />
                            {project.views}
                          </span>
                        </div>
                        <Button variant="ghost" size="sm">
                          View Details
                        </Button>
                      </div>
                    </div>
                  ))}
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="activity" className="space-y-6">
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <TrendingUp className="h-5 w-5" />
                    Recent Activity
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {recentActivity.map((activity, index) => (
                      <div key={index} className="flex items-start gap-3 p-3 bg-muted/30 rounded-lg">
                        <div className="mt-1">
                          {activity.type === "project" && <Lightbulb className="h-4 w-4 text-purple-500" />}
                          {activity.type === "event" && <Calendar className="h-4 w-4 text-blue-500" />}
                          {activity.type === "collaboration" && <Users className="h-4 w-4 text-green-500" />}
                          {activity.type === "achievement" && <Award className="h-4 w-4 text-orange-500" />}
                        </div>
                        <div className="flex-1">
                          <p className="text-sm">
                            <span className="text-muted-foreground">{activity.action}</span>
                            <span className="font-medium ml-1">"{activity.title}"</span>
                          </p>
                          <p className="text-xs text-muted-foreground flex items-center gap-1 mt-1">
                            <Clock className="h-3 w-3" />
                            {activity.timeAgo}
                          </p>
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="achievements" className="space-y-6">
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Award className="h-5 w-5" />
                    Achievement Progress
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="grid gap-6">
                    {badges.map((badge, index) => (
                      <div key={index} className="space-y-3">
                        <div className="flex items-center justify-between">
                          <div className="flex items-center gap-3">
                            <div className={`p-2 rounded-lg ${badge.color}`}>
                              <badge.icon className="h-5 w-5" />
                            </div>
                            <div>
                              <h3 className="font-medium flex items-center gap-2">
                                {badge.name}
                                {badge.earned && <Star className="h-4 w-4 text-yellow-500 fill-current" />}
                              </h3>
                              <p className="text-sm text-muted-foreground">{badge.description}</p>
                              {badge.earned && badge.earnedDate && (
                                <p className="text-xs text-green-600 dark:text-green-400">
                                  Earned on {badge.earnedDate}
                                </p>
                              )}
                            </div>
                          </div>
                          <div className="text-right">
                            <div className="text-sm font-medium">{badge.progress}%</div>
                            {badge.earned && (
                              <Badge className="bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400 text-xs">
                                Earned
                              </Badge>
                            )}
                          </div>
                        </div>
                        <Progress value={badge.progress} className="h-2" />
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </TabsContent>
          </Tabs>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Earned Badges */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Award className="h-5 w-5" />
                Earned Badges
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 gap-3">
                {badges.filter(badge => badge.earned).map((badge, index) => (
                  <div key={index} className={`p-3 rounded-lg text-center ${badge.color}`}>
                    <badge.icon className="h-6 w-6 mx-auto mb-2" />
                    <p className="text-xs font-medium">{badge.name}</p>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Next Goals */}
          <Card>
            <CardHeader>
              <CardTitle>Next Goals</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              {badges.filter(badge => !badge.earned).slice(0, 3).map((badge, index) => (
                <div key={index} className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">{badge.name}</span>
                    <span className="text-xs text-muted-foreground">{badge.progress}%</span>
                  </div>
                  <Progress value={badge.progress} className="h-2" />
                </div>
              ))}
            </CardContent>
          </Card>

          {/* Quick Actions */}
          <Card>
            <CardHeader>
              <CardTitle>Quick Actions</CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <Button variant="outline" className="w-full justify-start gap-2" asChild>
                <Link to="/projects/create">
                  <Lightbulb className="h-4 w-4" />
                  Share New Project
                </Link>
              </Button>
              <Button variant="outline" className="w-full justify-start gap-2" asChild>
                <Link to="/events">
                  <Calendar className="h-4 w-4" />
                  Browse Events
                </Link>
              </Button>
              <Button variant="outline" className="w-full justify-start gap-2" asChild>
                <Link to="/projects">
                  <Users className="h-4 w-4" />
                  Find Collaborators
                </Link>
              </Button>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}