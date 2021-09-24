package events

class EventRecurrence {
    Integer day
    Integer week
    Integer month

    static belongsTo = [event: Event]
    static constraints = {
        event(nullable: true)
        day(nullable: true)
        day validator: { val, obj, errors ->
            if (obj?.event?.recurringType?.recurringType?.equals("weekly") && val != null && val > 6) {
                errors?.rejectValue('day', 'invalid', 'Day of the week: valid values 0 to 6')
            }

        }
        week(nullable: true)
        week validator: { val, obj, errors ->
            if (obj?.event?.recurringType?.recurringType?.equals("weekly") && val != null) {
                errors?.rejectValue('day', 'invalid', 'You must not specify week when recurrance is weekly')
            }
        }
        month(nullable: true)
        month validator: { val, obj, errors ->
            if (obj?.event?.recurringType?.recurringType?.equals("monthly") && val != null) {
                errors?.rejectValue('day', 'invalid', 'You must not specify month when recurrance is monthly')
            }
        }
    }

}
