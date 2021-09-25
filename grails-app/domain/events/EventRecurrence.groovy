package events

class EventRecurrence {
    Integer day
    Integer week
    Integer month

    static belongsTo = [event: Event]
    static constraints = {
        event(nullable: true)
        day(nullable: true, range: 1..31)
        day validator: { val, obj, errors ->
            if (obj?.event?.recurringType?.recurringType?.equals("weekly") && val != null && val > 7) {
                errors?.rejectValue('day', 'invalid', 'Day of the week: valid values 1 to 7')
            }

        }
        week(nullable: true)
        week validator: { val, obj, errors ->
            if (obj?.event?.recurringType?.recurringType?.equals("weekly") && val != null) {
                errors?.rejectValue('week', 'invalid', 'You must not specify week when recurrance is weekly')
            }
        }
        month(nullable: true)
        month validator: { val, obj, errors ->
            if (obj?.event?.recurringType?.recurringType?.equals("monthly") && val != null) {
                errors?.rejectValue('month', 'invalid', 'You must not specify month when recurrance is monthly')
            }
        }
    }

}
