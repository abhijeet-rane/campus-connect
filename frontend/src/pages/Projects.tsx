import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Link } from "react-router-dom";
import { 
  Lightbulb, 
  Plus, 
  Search, 
  Heart, 
  MessageCircle, 
  Share2,
  Filter,
  Star,
  TrendingUp,
  Clock,
  Eye,
  Code,
  Smartphone,
  Globe,
  Database,
  Zap,
  Users
} from "lucide-react";

export default function Projects() {
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [searchQuery, setSearchQuery] = useState("");
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);

  const categories = [
    { id: "all", name: "All Projects", icon: Lightbulb },
    { id: "web", name: "Web Dev", icon: Globe },
    { id: "mobile", name: "Mobile", icon: Smartphone },
    { id: "ai", name: "AI/ML", icon: Zap },
    { id: "data", name: "Data Science", icon: Database },
    { id: "other", name: "Other", icon: Code }
  ];

  const projects = [
    {
      id: 1,
      title: "AI-Powered Study Assistant",
      description: "An intelligent chatbot that helps students with their coursework by answering questions, providing explanations, and suggesting study materials based on their learning patterns.",
      author: {
        name: "Sarah Chen",
        avatar: "",
        year: "Computer Science, 3rd Year"
      },
      category: "ai",
      tags: ["AI", "Education", "Chatbot", "Machine Learning"],
      likes: 34,
      comments: 12,
      views: 156,
      timeAgo: "2 hours ago",
      status: "seeking-collaborators",
      featured: true,
      liked: false,
      difficulty: "Advanced"
    },
    {
      id: 2,
      title: "Campus Sustainability Tracker",
      description: "A mobile app that tracks and gamifies sustainable practices on campus. Students can log eco-friendly activities, compete with friends, and earn rewards.",
      author: {
        name: "Mike Johnson",
        avatar: "",
        year: "Environmental Engineering, 2nd Year"
      },
      category: "mobile",
      tags: ["Mobile App", "Sustainability", "Gamification", "React Native"],
      likes: 28,
      comments: 8,
      views: 203,
      timeAgo: "5 hours ago",
      status: "in-development",
      featured: false,
      liked: true,
      difficulty: "Intermediate"
    },
    {
      id: 3,
      title: "Virtual Reality Lab Experience",
      description: "Create immersive VR experiences for chemistry and physics labs, allowing students to conduct experiments in a safe virtual environment.",
      author: {
        name: "Alex Rodriguez",
        avatar: "",
        year: "Computer Graphics, 4th Year"
      },
      category: "other",
      tags: ["VR", "Education", "3D Modeling", "Unity"],
      likes: 42,
      comments: 15,
      views: 298,
      timeAgo: "1 day ago",
      status: "completed",
      featured: true,
      liked: false,
      difficulty: "Advanced"
    },
    {
      id: 4,
      title: "Smart Campus Navigation",
      description: "An indoor navigation system for campus buildings using AR and beacon technology. Helps students find classrooms, offices, and facilities easily.",
      author: {
        name: "Emma Wilson",
        avatar: "",
        year: "Software Engineering, 3rd Year"
      },
      category: "mobile",
      tags: ["AR", "Navigation", "Beacons", "Flutter"],
      likes: 19,
      comments: 6,
      views: 134,
      timeAgo: "3 days ago",
      status: "seeking-collaborators",
      featured: false,
      liked: true,
      difficulty: "Intermediate"
    },
    {
      id: 5,
      title: "Student Expense Analytics Platform",
      description: "A web platform that analyzes student spending patterns and provides insights for better financial management, with integration to campus payment systems.",
      author: {
        name: "David Kim",
        avatar: "",
        year: "Data Science, 2nd Year"
      },
      category: "data",
      tags: ["Data Analytics", "Finance", "Dashboard", "Python"],
      likes: 25,
      comments: 9,
      views: 167,
      timeAgo: "1 week ago",
      status: "in-development",
      featured: false,
      liked: false,
      difficulty: "Intermediate"
    },
    {
      id: 6,
      title: "Collaborative Study Rooms Booking",
      description: "A comprehensive web application for booking and managing study rooms with real-time availability, group formation features, and resource sharing.",
      author: {
        name: "Lisa Zhang",
        avatar: "",
        year: "Information Systems, 4th Year"
      },
      category: "web",
      tags: ["Web App", "Booking System", "Real-time", "Node.js"],
      likes: 31,
      comments: 11,
      views: 221,
      timeAgo: "4 days ago",
      status: "seeking-collaborators",
      featured: true,
      liked: false,
      difficulty: "Intermediate"
    }
  ];

  const filteredProjects = projects.filter(project => {
    const matchesCategory = selectedCategory === "all" || project.category === selectedCategory;
    const matchesSearch = project.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         project.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         project.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()));
    return matchesCategory && matchesSearch;
  });

  const featuredProjects = projects.filter(project => project.featured);

  const getStatusColor = (status: string) => {
    switch (status) {
      case "seeking-collaborators": return "bg-blue-100 text-blue-700 dark:bg-blue-900/20 dark:text-blue-400";
      case "in-development": return "bg-yellow-100 text-yellow-700 dark:bg-yellow-900/20 dark:text-yellow-400";
      case "completed": return "bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400";
      default: return "bg-gray-100 text-gray-700 dark:bg-gray-900/20 dark:text-gray-400";
    }
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case "seeking-collaborators": return "Seeking Collaborators";
      case "in-development": return "In Development";
      case "completed": return "Completed";
      default: return status;
    }
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case "Beginner": return "text-green-600 dark:text-green-400";
      case "Intermediate": return "text-yellow-600 dark:text-yellow-400";
      case "Advanced": return "text-red-600 dark:text-red-400";
      default: return "text-gray-600 dark:text-gray-400";
    }
  };

  return (
    <div className="container py-8">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-bold mb-2">Project Ideas Hub</h1>
          <p className="text-muted-foreground">
            Share innovative project ideas and collaborate with talented peers
          </p>
        </div>
        <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
          <DialogTrigger asChild>
            <Button className="bg-gradient-primary gap-2" asChild>
              <Link to="/projects/create">
                <Plus className="h-4 w-4" />
                Share New Idea
              </Link>
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Share Your Project Idea</DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <div>
                <label className="text-sm font-medium mb-2 block">Project Title</label>
                <Input placeholder="Enter your project title..." />
              </div>
              <div>
                <label className="text-sm font-medium mb-2 block">Description</label>
                <Textarea 
                  placeholder="Describe your project idea, its goals, and potential impact..." 
                  rows={4}
                />
              </div>
              <div>
                <label className="text-sm font-medium mb-2 block">Tags</label>
                <Input placeholder="Add tags separated by commas (e.g., React, AI, Mobile)" />
              </div>
              <div className="flex gap-2">
                <Button className="bg-gradient-primary flex-1">
                  Publish Idea
                </Button>
                <Button variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
                  Cancel
                </Button>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </div>

      {/* Search and Filter */}
      <div className="flex flex-col md:flex-row gap-4 mb-8">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search project ideas..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-9"
          />
        </div>
        <Button variant="outline" className="gap-2">
          <Filter className="h-4 w-4" />
          Filters
        </Button>
      </div>

      {/* Categories */}
      <Tabs value={selectedCategory} onValueChange={setSelectedCategory} className="mb-8">
        <TabsList className="grid grid-cols-3 md:grid-cols-6 w-full">
          {categories.map((category) => (
            <TabsTrigger key={category.id} value={category.id} className="gap-2">
              <category.icon className="h-4 w-4" />
              <span className="hidden sm:inline">{category.name}</span>
            </TabsTrigger>
          ))}
        </TabsList>
      </Tabs>

      {/* Featured Projects */}
      {selectedCategory === "all" && (
        <section className="mb-8">
          <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
            <Star className="h-5 w-5 text-yellow-500" />
            Featured Project Ideas
          </h2>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {featuredProjects.map((project) => (
              <Card key={project.id} className="hover-lift border-primary/20 relative overflow-hidden">
                <div className="absolute top-2 right-2 z-10">
                  <Badge className="bg-gradient-primary">Featured</Badge>
                </div>
                <div className="absolute inset-0 bg-gradient-primary opacity-5"></div>
                <CardHeader className="relative">
                  <CardTitle className="text-lg mb-2">{project.title}</CardTitle>
                  <p className="text-sm text-muted-foreground line-clamp-3">
                    {project.description}
                  </p>
                </CardHeader>
                <CardContent className="relative space-y-4">
                  <div className="flex items-center gap-2">
                    <Avatar className="h-6 w-6">
                      <AvatarFallback className="text-xs">
                        {project.author.name.split(' ').map(n => n[0]).join('')}
                      </AvatarFallback>
                    </Avatar>
                    <div className="text-sm">
                      <p className="font-medium">{project.author.name}</p>
                      <p className="text-muted-foreground text-xs">{project.author.year}</p>
                    </div>
                  </div>

                  <div className="flex gap-1 flex-wrap">
                    {project.tags.slice(0, 3).map((tag, index) => (
                      <Badge key={index} variant="secondary" className="text-xs">
                        {tag}
                      </Badge>
                    ))}
                    {project.tags.length > 3 && (
                      <Badge variant="secondary" className="text-xs">
                        +{project.tags.length - 3}
                      </Badge>
                    )}
                  </div>

                  <div className="flex items-center justify-between text-sm text-muted-foreground">
                    <div className="flex items-center gap-4">
                      <span className="flex items-center gap-1">
                        <Heart className={`h-3 w-3 ${project.liked ? 'fill-red-500 text-red-500' : ''}`} />
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
                  </div>

                  <Button size="sm" variant="outline" className="w-full">
                    View Details
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        </section>
      )}

      {/* All Projects */}
      <section>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-2xl font-bold">
            {selectedCategory === "all" ? "All Project Ideas" : `${categories.find(c => c.id === selectedCategory)?.name} Projects`}
          </h2>
          <div className="flex gap-2">
            <Button variant="ghost" size="sm" className="gap-2">
              <TrendingUp className="h-4 w-4" />
              Trending
            </Button>
            <Button variant="ghost" size="sm" className="gap-2">
              <Clock className="h-4 w-4" />
              Recent
            </Button>
          </div>
        </div>

        <div className="space-y-6">
          {filteredProjects.map((project) => (
            <Card key={project.id} className="hover-lift">
              <CardContent className="p-6">
                <div className="flex flex-col lg:flex-row gap-6">
                  <div className="flex-1">
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <h3 className="text-xl font-semibold">{project.title}</h3>
                          {project.featured && (
                            <Badge className="bg-gradient-primary text-xs">Featured</Badge>
                          )}
                        </div>
                        <p className="text-muted-foreground mb-3">
                          {project.description}
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center gap-4 mb-4">
                      <div className="flex items-center gap-2">
                        <Avatar className="h-8 w-8">
                          <AvatarFallback className="text-sm">
                            {project.author.name.split(' ').map(n => n[0]).join('')}
                          </AvatarFallback>
                        </Avatar>
                        <div>
                          <p className="font-medium text-sm">{project.author.name}</p>
                          <p className="text-xs text-muted-foreground">{project.author.year}</p>
                        </div>
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {project.timeAgo}
                      </div>
                    </div>

                    <div className="flex flex-wrap gap-2 mb-4">
                      {project.tags.map((tag, index) => (
                        <Badge key={index} variant="secondary" className="text-xs">
                          {tag}
                        </Badge>
                      ))}
                    </div>

                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                      <span className="flex items-center gap-1">
                        <Heart className={`h-4 w-4 ${project.liked ? 'fill-red-500 text-red-500' : ''}`} />
                        {project.likes}
                      </span>
                      <span className="flex items-center gap-1">
                        <MessageCircle className="h-4 w-4" />
                        {project.comments}
                      </span>
                      <span className="flex items-center gap-1">
                        <Eye className="h-4 w-4" />
                        {project.views} views
                      </span>
                      <span className={`font-medium ${getDifficultyColor(project.difficulty)}`}>
                        {project.difficulty}
                      </span>
                    </div>
                  </div>

                  <div className="lg:w-64 space-y-3">
                    <Badge className={getStatusColor(project.status)}>
                      {getStatusText(project.status)}
                    </Badge>

                    <div className="flex flex-col gap-2">
                      <Button variant="outline" className="w-full gap-2">
                        <Users className="h-4 w-4" />
                        Collaborate
                      </Button>
                      <div className="grid grid-cols-2 gap-2">
                        <Button variant="ghost" size="sm">
                          <Heart className="h-4 w-4" />
                        </Button>
                        <Button variant="ghost" size="sm">
                          <Share2 className="h-4 w-4" />
                        </Button>
                      </div>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredProjects.length === 0 && (
          <div className="text-center py-12">
            <Lightbulb className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
            <h3 className="text-lg font-medium mb-2">No project ideas found</h3>
            <p className="text-muted-foreground">
              Try adjusting your search or be the first to share an idea in this category!
            </p>
          </div>
        )}
      </section>
    </div>
  );
}