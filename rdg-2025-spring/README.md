## Spring application routes and diagrams

### Completed routes

**Venues**
* POST ("/venues") - create new venue
* GET ("/venues") - get all venues
* GET ("/venues/{id}") - get specific venue and associated data
* PATCH ("/venues/{id}") - update venue
* DELETE ("/venues/{id}") - delete venue and set associated data venue id field to null

**Productions**
* POST ("/productions") - create new production
* GET ("/productions") - get all productions
* GET ("/productions/{id}) - get specific production
* PATCH ("/productions/{id}) - update production
* DELETE ("/productions/{id}) - delete production

**Festivals**
* POST ("/festivals") - create new festival
* GET ("/festivals") - get all festivals
* GET ("/festivals") - get specific festival
* PATCH ("/festivals/{id}") - update festival
* DELETE ("/festivals/{id}) - delete festival

**Performances**
* POST ("/performances") - add new performance

### Diagrams

#### Venues

![post venue diagram](../images/post-venue-diagram.jpg)

![get venues diagram](../images/get-venues-diagram.jpg)

![delete venue diagram](../images/delete-venue-diagram.jpg)

![get venue diagram](../images/get-venue-diagram.jpg)

![patch venue diagram](../images/patch-venue-diagram.jpg)

#### Productions

![post production diagram](../images/post-production-diagram.jpg)

![get productions diagram](../images/get-productions-diagram.jpg)

![get production diagram](../images/get-production-diagram.jpg)

![patch production diagram](../images/patch-production-diagram.jpg)

![delete production diagram](../images/delete-production-diagram.jpg)

#### Festivals

![post festival diagram](../images/post-festival-diagram.jpg)

![get festivals diagram](../images/get-festivals-diagram.jpg)

![get festival diagram](../images/get-festival-diagram.jpg)

![patch festival diagram](../images/patch-festival-diagram.jpg)

![delete festival diagram](../images/delete-festival-diagram.jpg)

### Performances

![post performance diagram](../images/post-performance-diagram.jpg)
