package events

import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        def recurringTypes = ['once', 'daily', 'weekly', 'monthly', 'yearly']
        recurringTypes.each { recurringType ->
            RecurringType recurringTypeFromDb = RecurringType.findByRecurringType(recurringType)
            if (!recurringTypeFromDb) {
                recurringTypeFromDb = new RecurringType(recurringType: recurringType)
                recurringTypeFromDb.save(flush: true)
            }
        }
        registerMarshallers()
    }

    private void registerMarshallers() {
        JSON.registerObjectMarshaller(Event) {

            def map = [
                    'id'              : it.id ?: "",
                    'eventName'       : it.eventName ?: "",
                    'eventDescription': it.eventDescription ?: "",
                    'startDate'       : it.startDate ?: "",
                    'endDate'         : it.endDate ?: "",
                    'startTime'       : it.startTime ?: "",
                    'endTime'         : it.endTime ?: "",
                    'recurringType'   : it.recurringType ?: "",
                    'separation'      : it.separation ?: "",
                    'maxCount'        : it.maxCount ?: "",
                    'isWholeDay'      : it.isWholeDay ?: false,
                    'isCancelled'     : it.isCancelled ?: false
            ]
            if (it.recurringType?.recurringType != "once") {
                map.put('eventRecurrence', it.eventRecurrence)
            }
            return map
        }
        JSON.registerObjectMarshaller(RecurringType) {

            def map = [
                    'recurrance': it.recurringType ?: ""

            ]
            return map
        }
        JSON.registerObjectMarshaller(EventRecurrence) {

            def map = [:]
            if (it.day) {
                map.put('day', it.day)
            }
            if (it.week) {
                map.put('week', it.week)
            }
            if (it.month) {
                map.put('month', it.month)
            }
            return map
        }
        def destroy = {
        }
    }
}
