import { useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Separator } from "@/components/ui/separator";
import { useToast } from "@/hooks/use-toast";
import { 
  ArrowLeft,
  Calendar, 
  Clock, 
  MapPin, 
  Users, 
  Heart,
  Share2,
  Star,
  CheckCircle,
  AlertCircle,
  User
} from "lucide-react";

export default function EventDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { toast } = useToast();
  const [isRegistered, setIsRegistered] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  // Mock event data - in real app, fetch based on id
  const event = {
    id: 1,
    title: "AI & Machine Learning Symposium",
    description: "Join industry experts discussing the future of artificial intelligence and its applications in various fields. This comprehensive symposium will cover cutting-edge research, practical applications, and career opportunities in AI/ML.",
    longDescription: `
      This symposium brings together leading researchers, industry professionals, and students to explore the rapidly evolving landscape of artificial intelligence and machine learning.

      **What You'll Learn:**
      • Latest breakthroughs in AI research
      • Practical applications across industries
      • Career paths and opportunities
      • Hands-on workshops with real datasets
      • Networking with industry professionals

      **Featured Speakers:**
      • Dr. Sarah Chen - AI Research Director at TechCorp
      • Prof. Michael Johnson - Stanford AI Lab
      • Lisa Rodriguez - ML Engineer at DataFlow
      • Alex Kim - Startup Founder & AI Entrepreneur

      **Schedule:**
      • 2:00 PM - Opening Keynote
      • 2:45 PM - Panel Discussion: AI Ethics
      • 3:30 PM - Break & Networking
      • 4:00 PM - Technical Workshops
      • 5:00 PM - Closing Remarks
    `,
    date: "2024-11-25",
    time: "2:00 PM - 5:00 PM",
    location: "Engineering Building, Hall A",
    category: "tech",
    organizer: {
      name: "CS Department",
      avatar: "",
      contact: "cs-events@university.edu"
    },
    attendees: 124,
    maxAttendees: 200,
    tags: ["AI", "Machine Learning", "Technology", "Research"],
    featured: true,
    requirements: "Basic understanding of programming concepts recommended but not required.",
    agenda: [
      { time: "2:00 PM", title: "Registration & Welcome", speaker: "Event Team" },
      { time: "2:15 PM", title: "Opening Keynote: The Future of AI", speaker: "Dr. Sarah Chen" },
      { time: "3:00 PM", title: "Panel: Ethics in AI Development", speaker: "Expert Panel" },
      { time: "3:45 PM", title: "Networking Break", speaker: "" },
      { time: "4:15 PM", title: "Hands-on ML Workshop", speaker: "Prof. Michael Johnson" },
      { time: "5:00 PM", title: "Q&A and Closing", speaker: "All Speakers" }
    ]
  };

  const handleRegister = async () => {
    setIsLoading(true);
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      setIsRegistered(!isRegistered);
      toast({
        title: isRegistered ? "Registration cancelled" : "Successfully registered!",
        description: isRegistered 
          ? "You have been removed from the event." 
          : "You're all set for the event. Check your email for details.",
      });
    } catch (error) {
      toast({
        title: "Registration failed",
        description: "Please try again later.",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleShare = () => {
    if (navigator.share) {
      navigator.share({
        title: event.title,
        text: event.description,
        url: window.location.href,
      });
    } else {
      navigator.clipboard.writeText(window.location.href);
      toast({
        title: "Link copied!",
        description: "Event link has been copied to your clipboard.",
      });
    }
  };

  const spotsRemaining = event.maxAttendees - event.attendees;
  const isAlmostFull = spotsRemaining <= 20;

  return (
    <div className="container py-8">
      {/* Header */}
      <div className="flex items-center gap-4 mb-6">
        <Button variant="ghost" size="icon" onClick={() => navigate(-1)}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">{event.title}</h1>
          <p className="text-muted-foreground">Event Details</p>
        </div>
      </div>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-6">
          {/* Event Info Card */}
          <Card>
            <CardHeader>
              <div className="flex items-start justify-between">
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-2">
                    <CardTitle className="text-2xl">{event.title}</CardTitle>
                    {event.featured && (
                      <Badge className="bg-gradient-primary">Featured</Badge>
                    )}
                  </div>
                  <p className="text-muted-foreground">{event.description}</p>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Key Details */}
              <div className="grid sm:grid-cols-2 gap-4">
                <div className="flex items-center gap-3 p-3 bg-muted/30 rounded-lg">
                  <Calendar className="h-5 w-5 text-primary" />
                  <div>
                    <p className="font-medium">Date</p>
                    <p className="text-sm text-muted-foreground">
                      {new Date(event.date).toLocaleDateString('en-US', { 
                        weekday: 'long', 
                        year: 'numeric', 
                        month: 'long', 
                        day: 'numeric' 
                      })}
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-3 p-3 bg-muted/30 rounded-lg">
                  <Clock className="h-5 w-5 text-primary" />
                  <div>
                    <p className="font-medium">Time</p>
                    <p className="text-sm text-muted-foreground">{event.time}</p>
                  </div>
                </div>
                <div className="flex items-center gap-3 p-3 bg-muted/30 rounded-lg">
                  <MapPin className="h-5 w-5 text-primary" />
                  <div>
                    <p className="font-medium">Location</p>
                    <p className="text-sm text-muted-foreground">{event.location}</p>
                  </div>
                </div>
                <div className="flex items-center gap-3 p-3 bg-muted/30 rounded-lg">
                  <Users className="h-5 w-5 text-primary" />
                  <div>
                    <p className="font-medium">Attendance</p>
                    <p className="text-sm text-muted-foreground">
                      {event.attendees}/{event.maxAttendees} registered
                    </p>
                  </div>
                </div>
              </div>

              {/* Availability Alert */}
              {isAlmostFull && (
                <div className="flex items-center gap-2 p-3 bg-warning/10 border border-warning/20 rounded-lg">
                  <AlertCircle className="h-4 w-4 text-warning" />
                  <p className="text-sm">
                    <span className="font-medium">Almost full!</span> Only {spotsRemaining} spots remaining.
                  </p>
                </div>
              )}

              {/* Tags */}
              <div className="flex gap-2 flex-wrap">
                {event.tags.map((tag, index) => (
                  <Badge key={index} variant="secondary">
                    {tag}
                  </Badge>
                ))}
              </div>

              <Separator />

              {/* Detailed Description */}
              <div>
                <h3 className="text-lg font-semibold mb-3">About This Event</h3>
                <div className="prose prose-sm max-w-none text-muted-foreground whitespace-pre-line">
                  {event.longDescription}
                </div>
              </div>

              {/* Requirements */}
              {event.requirements && (
                <div>
                  <h3 className="text-lg font-semibold mb-2">Requirements</h3>
                  <p className="text-muted-foreground">{event.requirements}</p>
                </div>
              )}

              {/* Agenda */}
              <div>
                <h3 className="text-lg font-semibold mb-3">Event Agenda</h3>
                <div className="space-y-3">
                  {event.agenda.map((item, index) => (
                    <div key={index} className="flex gap-4 p-3 border border-border rounded-lg">
                      <div className="text-sm font-medium text-primary min-w-20">
                        {item.time}
                      </div>
                      <div className="flex-1">
                        <p className="font-medium">{item.title}</p>
                        {item.speaker && (
                          <p className="text-sm text-muted-foreground">{item.speaker}</p>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Registration Card */}
          <Card>
            <CardHeader>
              <CardTitle>Registration</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {isRegistered && (
                <div className="flex items-center gap-2 p-3 bg-success/10 border border-success/20 rounded-lg">
                  <CheckCircle className="h-4 w-4 text-success" />
                  <p className="text-sm font-medium">You're registered!</p>
                </div>
              )}
              
              <Button 
                onClick={handleRegister}
                disabled={isLoading || (!isRegistered && spotsRemaining === 0)}
                className={`w-full ${!isRegistered ? 'bg-gradient-primary' : ''}`}
                variant={isRegistered ? "outline" : "default"}
              >
                {isLoading ? "Processing..." : 
                 isRegistered ? "Cancel Registration" : 
                 spotsRemaining === 0 ? "Event Full" : "Register Now"}
              </Button>

              <div className="flex gap-2">
                <Button variant="ghost" size="sm" className="flex-1">
                  <Heart className="h-4 w-4 mr-2" />
                  Save
                </Button>
                <Button variant="ghost" size="sm" className="flex-1" onClick={handleShare}>
                  <Share2 className="h-4 w-4 mr-2" />
                  Share
                </Button>
              </div>

              <div className="text-center pt-2">
                <p className="text-sm text-muted-foreground">
                  {spotsRemaining} of {event.maxAttendees} spots available
                </p>
              </div>
            </CardContent>
          </Card>

          {/* Organizer Card */}
          <Card>
            <CardHeader>
              <CardTitle>Organizer</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center gap-3 mb-4">
                <Avatar>
                  <AvatarFallback>
                    <User className="h-4 w-4" />
                  </AvatarFallback>
                </Avatar>
                <div>
                  <p className="font-medium">{event.organizer.name}</p>
                  <p className="text-sm text-muted-foreground">{event.organizer.contact}</p>
                </div>
              </div>
              <Button variant="outline" className="w-full">
                Contact Organizer
              </Button>
            </CardContent>
          </Card>

          {/* Related Events */}
          <Card>
            <CardHeader>
              <CardTitle>Related Events</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              <div className="p-3 border border-border rounded-lg hover-lift transition-smooth cursor-pointer">
                <h4 className="font-medium text-sm">Web Development Workshop</h4>
                <p className="text-xs text-muted-foreground">Dec 8, 2024</p>
              </div>
              <div className="p-3 border border-border rounded-lg hover-lift transition-smooth cursor-pointer">
                <h4 className="font-medium text-sm">Career Fair - Tech</h4>
                <p className="text-xs text-muted-foreground">Dec 5, 2024</p>
              </div>
              <Button variant="ghost" size="sm" className="w-full" asChild>
                <Link to="/events">View All Events</Link>
              </Button>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}