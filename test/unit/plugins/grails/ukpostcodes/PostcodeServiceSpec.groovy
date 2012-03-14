package plugins.grails.ukpostcodes

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PostcodeService)
@Mock(PostCode)
class PostcodeServiceSpec extends Specification {

    def setup() {

    }

    def "First check against the PostCode cache"() {
        given:
            def postCode = new PostCode(code: 'EC1N8TE', latitude: 0.0f, longitude: 0.0f, constituency: '', ward: '', district: '')
            postCode.save()
        expect:
            postCode == service.lookup('EC1N8TE')
    }

    def "Invalid postcodes return null"(){
        expect:
            null == service.lookup('NOTREAL')
    }

    def "new postcodes are created in db and correctly populated"(){

        expect:
            PostCode.count() == 0

        when:
            def postcode = service.lookup('W25HD')

        then:
            PostCode.count() == 1
            postcode.longitude == -0.193681f
            postcode.latitude == 51.521572f
            postcode.constituency == 'Westminster North'
            postcode.district == 'Westminster'
            postcode.ward == 'Westbourne'

    }

}
