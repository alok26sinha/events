package events

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.rest.RestfulController

import java.text.ParseException
import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

class EventController extends RestfulController {
    static namespace = 'v1'
    static responseFormats = ['json', 'xml']
    static allowedMethods = [index: "GET", search: "GET", save: "POST", upload: "POST", delete: "DELETE"]

    def eventService

    EventController() {
        super(Event)
    }

    def index() {

        log.info("EventController******GET")
        respond Event.findAll(), [status: OK]
    }

    def show(Long id) {
        log.info("EventController******GET")
        def event = Event.findById(id)

        if (event) {
            respond event, [status: OK]
        } else {
            render([message: 'No records found'] as JSON)
        }
    }

    def search(params) {


        def cri = Event.createCriteria()
        def data = cri.list() {
            params.each { field, value ->
                log.debug("Params: field=" + field + ",value=" + value + "]")

                if (field == "startDate") {
                    Date startDate
                    try {
                        startDate = new SimpleDateFormat("MM.dd.yyyy").parse(value)
                    }
                    catch (ParseException e) {
                        render([message: 'startDate should be in MM.dd.yyyy format'] as JSON)
                    }
                    ge(field, startDate)

                }
                if (field == "endDate") {

                    Date endDate

                    try {
                        endDate = new SimpleDateFormat("MM.dd.yyyy").parse(value)
                    }
                    catch (ParseException e) {
                        render([message: 'endDate should be in MM.dd.yyyy format'] as JSON)
                    }
                    le(field, endDate)

                }

            }
        }
        render(data as JSON)
    }

    def save() {

        log.debug("EventController******POST" + "  payload= " + request.JSON)
        Event eventInstance = request.JSON

        if (eventInstance == null) {
            render([message: 'Invalid Input'] as JSON)
            return
        }
        eventInstance.validate()
        if (eventInstance.hasErrors()) {
            render([message: eventInstance.errors] as JSON)
            return
        }

        eventService.createEvent(eventInstance)
        respond eventInstance, [status: CREATED]
    }

    @Transactional
    def update(Event eventInstance) {

        log.debug("EventController******POST" + "  payload= " + eventInstance)
        if (!eventInstance) {
            render([message: 'Invalid Input'] as JSON)
            return
        }
        eventInstance.validate()
        if (eventInstance.hasErrors()) {
            render([message: 'Invalid Input'] as JSON)
            return
        }

        eventInstance.save flush: true
        respond eventInstance, [status: OK]
    }
}