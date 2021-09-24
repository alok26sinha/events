package events

import grails.databinding.BindUsing

import java.sql.Timestamp
import java.text.SimpleDateFormat

class Event {

    String eventName
    String eventDescription
    @BindUsing({
        obj, source -> new SimpleDateFormat("MM.dd.yyyy").parse(source['startDate'])
    })
    Date startDate
    @BindUsing({
        obj, source -> new SimpleDateFormat("MM.dd.yyyy").parse(source['endDate'])
    })
    Date endDate
    @BindUsing({
        obj, source -> new Timestamp(new SimpleDateFormat("hh:mm").parse(source['startTime']).getTime())
    })
    Timestamp startTime
    @BindUsing({
        obj, source -> new Timestamp(new SimpleDateFormat("hh:mm").parse(source['endTime']).getTime())
    })
    Timestamp endTime

    @BindUsing({
        obj, source ->
            def recType = RecurringType.findByRecurringType(source["recurringType"].recurrance)
            if (source['recurringType'].recurrance != null && recType == null) {
                throw new Exception("Invalid recurringType!")
            }
            return recType
    })
    RecurringType recurringType
    @BindUsing({
        obj, source ->
            if (source['separation'] == null)
                return 1
    })
    Integer separation
    Integer maxCount
    Boolean isWholeDay
    Boolean isCancelled

    static mapping = {
        eventDescription sqlType: 'text'
    }
    static hasOne = [eventRecurrence: EventRecurrence]
    static constraints = {
        eventDescription(nullable: true)
        endDate(nullable: true)
        startTime(nullable: true)
        endTime(nullable: true)
        recurringType(nullable: false)
        eventRecurrence(nullable: true)
        separation(nullable: true, default: 1)
        maxCount(nullable: true)
        isWholeDay(nullable: false, default: false)
        isCancelled(nullable: false, default: false)
        eventRecurrence(validator: { val, obj ->
            if (obj.recurringType && !(obj.recurringType?.recurringType.equals("once") || obj.recurringType?.recurringType.equals("daily")) && val == null) {
                obj.errors.rejectValue('eventRecurrence', 'Invalid', 'Must provide eventRecurrence details')
            }
            if (obj.recurringType && (obj.recurringType?.recurringType.equals("once") || obj.recurringType?.recurringType.equals("daily")) && val != null) {
                obj.errors.rejectValue('eventRecurrence', 'Invalid', 'Must not provide eventRecurrence when recurrance is once or daily')
            }
        })
    }

}
