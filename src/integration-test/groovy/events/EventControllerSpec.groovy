package events


import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

@Integration
@Stepwise
class EventControllerSpec extends Specification {

    @Shared
    def rest = new RestBuilder()

    void "test that no event exists"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/api/v1/event")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 0
    }
    // end::no_events[]

    // tag::create_events[]
    void "test creating events"() {
        when:
        def resp = rest.post("http://localhost:${serverPort}/api/v1/event") {
            json {

                eventName = 'event-once'
                eventDescription = 'single event'
                recurringType =
                        {
                            recurrance = 'once'
                        }

                startDate = '10.10.2021'
                startTime = '09:00'
                endTime = '10:15'
                maxCount = 1
                isWholeDay = false
                isCancelled = false

            }
        }
        def contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.eventName == 'event-once'
        resp.json.eventDescription == 'single event'
        resp.json.recurringType.recurrance == 'once'
        resp.json.startDate == '10.10.2021'
        resp.json.startTime == '09:00'
        resp.json.endTime == '10:15'
        resp.json.maxCount == 1
        resp.json.isWholeDay == false
        resp.json.isCancelled == false

        when:
        resp = rest.post("http://localhost:${serverPort}/api/v1/event") {
            json {

                eventName = 'event-recurring-weekly-one-year'
                eventDescription = 'recurring-weekly-one-year'
                recurringType =
                        {
                            recurrance = 'weekly'
                        }
                eventRecurrence = {
                    day = 2

                }
                startDate = new SimpleDateFormat("MM.dd.yyyy").format(new Date())
                endDate = new SimpleDateFormat("MM.dd.yyyy").format(new Date() + Calendar.YEAR + 1)
                isWholeDay = true
                isCancelled = false

            }
        }
        contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.eventName == 'event-recurring-weekly-one-year'
        resp.json.eventDescription == 'recurring-weekly-one-year'
        resp.json.recurringType.recurrance == 'weekly'
        resp.json.eventRecurrence.day == 2
        resp.json.startDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date())
        resp.json.endDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date() + Calendar.YEAR + 1)
        resp.json.isWholeDay == true
        resp.json.isCancelled == false


        when:
        resp = rest.post("http://localhost:${serverPort}/api/v1/event") {
            json {

                eventName = 'event-recurring-monthly-one-year'
                eventDescription = 'recurring-monthly-one-year'
                recurringType =
                        {
                            recurrance = 'monthly'
                        }
                eventRecurrence = {
                    day = 1
                    week = 1

                }
                startDate = new SimpleDateFormat("MM.dd.yyyy").format(new Date())
                endDate = new SimpleDateFormat("MM.dd.yyyy").format(new Date() + Calendar.YEAR + 1)
                startTime = '00:00'
                endTime = '02:00'
                isWholeDay = false
                isCancelled = false

            }
        }
        contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.eventName == 'event-recurring-monthly-one-year'
        resp.json.eventDescription == 'recurring-monthly-one-year'
        resp.json.recurringType.recurrance == 'monthly'
        resp.json.eventRecurrence.day == 1
        resp.json.eventRecurrence.week == 1
        resp.json.startDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date())
        resp.json.endDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date() + Calendar.YEAR + 1)
        resp.json.isWholeDay == false
        resp.json.isCancelled == false
    }

    void 'test retrieving list of events'() {
        when:
        Map queryParams = [startDate: '\'10.10.2021\'', endDate: new SimpleDateFormat("MM.dd.yyyy").format(new Date() + Calendar.YEAR + 1)]
        def resp = rest.get("http://localhost:${serverPort}/api/v1/event/search", queryParams)
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 3

        and:
        resp.json[0].eventName == 'event-once'
        resp.json[0].eventDescription == 'single event'
        resp.json[0].recurringType.recurrance == 'once'
        resp.json[0].startDate == '10.10.2021'
        resp.json[0].startTime == '09:00'
        resp.json[0].endTime == '10:15'
        resp.json[0].maxCount == 1
        resp.json[0].isWholeDay == false
        resp.json[0].isCancelled == false

        and:
        resp.json[1].eventName == 'event-recurring-weekly-one-year'
        resp.json[1].eventDescription == 'recurring-weekly-one-year'
        resp.json[1].recurringType.recurrance == 'weekly'
        resp.json[1].eventRecurrence.day == 2
        resp.json[1].startDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date())
        resp.json[1].endDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date() + Calendar.YEAR + 1)
        resp.json[1].isWholeDay == true
        resp.json[1].isCancelled == false

        and:
        resp.json[2].eventName == 'event-recurring-monthly-one-year'
        resp.json[2].eventDescription == 'recurring-monthly-one-year'
        resp.json[2].recurringType.recurrance == 'monthly'
        resp.json[2].eventRecurrence.day == 1
        resp.json[2].eventRecurrence.week == 1
        resp.json[2].startDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date())
        resp.json[2].endDate == new SimpleDateFormat("MM.dd.yyyy").format(new Date() + Calendar.YEAR + 1)
        resp.json[2].isWholeDay == false
        resp.json[2].isCancelled == false
    }
}