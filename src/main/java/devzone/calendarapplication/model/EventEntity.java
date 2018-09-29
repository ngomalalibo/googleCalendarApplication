package devzone.calendarapplication.model;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.Data;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity(value = "events", noClassnameStored = true)
@Indexes(@Index(fields = {@Field(value = "id")}))
public class EventEntity extends BaseEntity
{
    public EventEntity()
    {
    }
    
    private String colorId;
    
    private DateTime created;
    
    private Event.Creator creator;
    
    private String description;
    
    private EventDateTime end;
    
    private String etag;
    
    private String hangoutLink;
    
    private String htmlLink;
    
    private String kind;
    
    private String location;
    
    private Event.Organizer organizer;
    
    private EventDateTime originalStartTime;
    
    private List<String> recurrence;
    
    private Event.Reminders reminders;
    
    private Event.Source source;
    
    private EventDateTime start;
    
    private String status;
    
    private String summary;
    
    private String transparency;
    
    private DateTime updated;
    
    private String visibility;
}
