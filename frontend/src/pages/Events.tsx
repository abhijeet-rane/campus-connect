import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Link } from "react-router-dom";
import { 
  Calendar, 
  Clock, 
  MapPin, 
  Users, 
  Search,
  Filter,
  Star,
  Heart,
  Share2,
  Plus,
  Zap,
  BookOpen,
  Code,
  Briefcase,
  Music
} from "lucide-react";

export default function Events() {
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [searchQuery, setSearchQuery] = useState("");

  const categories = [
    { id: "all", name: "All Events", icon: Calendar },
    { id: "tech", name: "Technology", icon: Code },
    { id: "academic", name: "Academic", icon: BookOpen },
    { id: "career", name: "Career", icon: Briefcase },
    { id: "social", name: "Social", icon: Music },
    { id: "workshop", name: "Workshops", icon: Zap }
  ];

  const events = [
    {
      id: 1,
      title: "AI & Machine Learning Symposium",
      description: "Join industry experts discussing the future of artificial intelligence and its applications in various fields.",
      date: "2024-11-25",
      time: "2:00 PM - 5:00 PM",
      location: "Engineering Building, Hall A",
      category: "tech",
      organizer: "CS Department",
      attendees: 124,
      maxAttendees: 200,
      tags: ["AI", "Machine Learning", "Technology"],
      featured: true,
      registered: true
    },
    {
      id: 2,
      title: "Startup Pitch Competition",
      description: "Present your innovative startup ideas to a panel of investors and industry professionals.",
      date: "2024-11-28",
      time: "10:00 AM - 4:00 PM",
      location: "Business School Auditorium",
      category: "career",
      organizer: "Entrepreneurship Club",
      attendees: 89,
      maxAttendees: 150,
      tags: ["Entrepreneurship", "Pitch", "Startup"],
      featured: false,
      registered: false
    },
    {
      id: 3,
      title: "Research Methodology Workshop",
      description: "Learn essential research skills and methodologies for your academic projects.",
      date: "2024-12-02",
      time: "9:00 AM - 12:00 PM",
      location: "Library Conference Room",
      category: "academic",
      organizer: "Graduate Office",
      attendees: 45,
      maxAttendees: 60,
      tags: ["Research", "Academic", "Skills"],
      featured: false,
      registered: true
    },
    {
      id: 4,
      title: "Career Fair - Tech Companies",
      description: "Meet recruiters from top technology companies and explore internship opportunities.",
      date: "2024-12-05",
      time: "11:00 AM - 6:00 PM",
      location: "Student Center Main Hall",
      category: "career",
      organizer: "Career Services",
      attendees: 312,
      maxAttendees: 500,
      tags: ["Career", "Internship", "Technology"],
      featured: true,
      registered: false
    },
    {
      id: 5,
      title: "Web Development Bootcamp",
      description: "Intensive hands-on workshop covering modern web development frameworks and best practices.",
      date: "2024-12-08",
      time: "1:00 PM - 6:00 PM",
      location: "Computer Lab 2",
      category: "workshop",
      organizer: "Tech Club",
      attendees: 67,
      maxAttendees: 80,
      tags: ["Web Dev", "React", "JavaScript"],
      featured: false,
      registered: false
    },
    {
      id: 6,
      title: "Annual Winter Gala",
      description: "Celebrate the end of the semester with music, food, and networking opportunities.",
      date: "2024-12-15",
      time: "7:00 PM - 11:00 PM",
      location: "Grand Ballroom",
      category: "social",
      organizer: "Student Government",
      attendees: 234,
      maxAttendees: 400,
      tags: ["Social", "Networking", "Celebration"],
      featured: true,
      registered: false
    }
  ];

  const filteredEvents = events.filter(event => {
    const matchesCategory = selectedCategory === "all" || event.category === selectedCategory;
    const matchesSearch = event.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         event.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         event.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()));
    return matchesCategory && matchesSearch;
  });

  const featuredEvents = events.filter(event => event.featured);

  return (
    <div className="container py-8">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-bold mb-2">Campus Events</h1>
          <p className="text-muted-foreground">
            Discover and join exciting events happening around campus
          </p>
        </div>
        <Button className="bg-gradient-primary gap-2">
          <Plus className="h-4 w-4" />
          Create Event
        </Button>
      </div>

      {/* Search and Filter */}
      <div className="flex flex-col md:flex-row gap-4 mb-8">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search events..."
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

      {/* Featured Events */}
      {selectedCategory === "all" && (
        <section className="mb-8">
          <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
            <Star className="h-5 w-5 text-yellow-500" />
            Featured Events
          </h2>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {featuredEvents.map((event) => (
              <Card key={event.id} className="hover-lift border-primary/20 relative overflow-hidden">
                <div className="absolute top-2 right-2 z-10">
                  <Badge className="bg-gradient-primary">Featured</Badge>
                </div>
                <div className="absolute inset-0 bg-gradient-primary opacity-5"></div>
                <CardHeader className="relative">
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-lg mb-2">{event.title}</CardTitle>
                      <p className="text-sm text-muted-foreground line-clamp-2">
                        {event.description}
                      </p>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="relative space-y-4">
                  <div className="space-y-2 text-sm">
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Calendar className="h-4 w-4" />
                      {new Date(event.date).toLocaleDateString()}
                    </div>
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Clock className="h-4 w-4" />
                      {event.time}
                    </div>
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <MapPin className="h-4 w-4" />
                      {event.location}
                    </div>
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Users className="h-4 w-4" />
                      {event.attendees}/{event.maxAttendees} attending
                    </div>
                  </div>

                  <div className="flex gap-1 flex-wrap">
                    {event.tags.map((tag, index) => (
                      <Badge key={index} variant="secondary" className="text-xs">
                        {tag}
                      </Badge>
                    ))}
                  </div>

                  <div className="flex justify-between items-center pt-2">
                    <div className="flex gap-2">
                      <Button variant="ghost" size="sm">
                        <Heart className="h-4 w-4" />
                      </Button>
                      <Button variant="ghost" size="sm">
                        <Share2 className="h-4 w-4" />
                      </Button>
                    </div>
                    <Button 
                      size="sm" 
                      variant={event.registered ? "secondary" : "default"}
                      className={!event.registered ? "bg-gradient-primary" : ""}
                      asChild
                    >
                      <Link to={`/events/${event.id}`}>
                        {event.registered ? "View Details" : "Join Event"}
                      </Link>
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </section>
      )}

      {/* All Events */}
      <section>
        <h2 className="text-2xl font-bold mb-4">
          {selectedCategory === "all" ? "All Events" : `${categories.find(c => c.id === selectedCategory)?.name} Events`}
        </h2>
        <div className="grid gap-6">
          {filteredEvents.map((event) => (
            <Card key={event.id} className="hover-lift">
              <CardContent className="p-6">
                <div className="flex flex-col lg:flex-row gap-6">
                  <div className="flex-1">
                    <div className="flex items-start justify-between mb-3">
                      <div>
                        <h3 className="text-xl font-semibold mb-2">{event.title}</h3>
                        <p className="text-muted-foreground mb-3">
                          {event.description}
                        </p>
                      </div>
                      {event.featured && (
                        <Badge className="bg-gradient-primary ml-4">Featured</Badge>
                      )}
                    </div>

                    <div className="grid sm:grid-cols-2 gap-2 mb-4 text-sm">
                      <div className="flex items-center gap-2 text-muted-foreground">
                        <Calendar className="h-4 w-4" />
                        {new Date(event.date).toLocaleDateString()}
                      </div>
                      <div className="flex items-center gap-2 text-muted-foreground">
                        <Clock className="h-4 w-4" />
                        {event.time}
                      </div>
                      <div className="flex items-center gap-2 text-muted-foreground">
                        <MapPin className="h-4 w-4" />
                        {event.location}
                      </div>
                      <div className="flex items-center gap-2 text-muted-foreground">
                        <Users className="h-4 w-4" />
                        {event.attendees}/{event.maxAttendees} attending
                      </div>
                    </div>

                    <div className="flex items-center gap-4">
                      <div className="flex items-center gap-2">
                        <Avatar className="h-6 w-6">
                          <AvatarFallback className="text-xs">
                            {event.organizer.split(' ').map(n => n[0]).join('')}
                          </AvatarFallback>
                        </Avatar>
                        <span className="text-sm text-muted-foreground">
                          Organized by {event.organizer}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="lg:w-64 space-y-4">
                    <div className="flex gap-1 flex-wrap">
                      {event.tags.map((tag, index) => (
                        <Badge key={index} variant="secondary" className="text-xs">
                          {tag}
                        </Badge>
                      ))}
                    </div>

                    <div className="flex flex-col gap-2">
                      <Button 
                        className={`w-full ${event.registered ? "" : "bg-gradient-primary"}`}
                        variant={event.registered ? "secondary" : "default"}
                        asChild
                      >
                        <Link to={`/events/${event.id}`}>
                          {event.registered ? "View Details" : "Join Event"}
                        </Link>
                      </Button>
                      <div className="flex gap-2">
                        <Button variant="ghost" size="sm" className="flex-1">
                          <Heart className="h-4 w-4 mr-2" />
                          Save
                        </Button>
                        <Button variant="ghost" size="sm" className="flex-1">
                          <Share2 className="h-4 w-4 mr-2" />
                          Share
                        </Button>
                      </div>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredEvents.length === 0 && (
          <div className="text-center py-12">
            <Calendar className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
            <h3 className="text-lg font-medium mb-2">No events found</h3>
            <p className="text-muted-foreground">
              Try adjusting your search or filter criteria
            </p>
          </div>
        )}
      </section>
    </div>
  );
}