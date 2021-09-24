package events

import grails.gorm.transactions.Transactional

@Transactional
class EventService {

    def createEvent(Event eventInstance) {

        eventInstance.save()
    }
}
