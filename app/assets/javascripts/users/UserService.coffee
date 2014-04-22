
class UserService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserService"

    listUsers: () ->
        @$log.debug "listUsers()"
        deferred = @$q.defer()

        @$http.get("/users")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Users - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Users - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    getUser: (id) ->
        @$log.debug "getUser()"
        @$log.debug("Here is the id: #{id}")
        deferred = @$q.defer()

        @$http.get("/user/#{id}")
        .success((data, status, headers) =>
                @$log.info("Successfully retrieved User - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to retrieve User - status #{status}")
                deferred.reject(data);
            )
        deferred.promise        

    createUser: (user) ->
        @$log.debug "createUser #{angular.toJson(user, true)}"
        deferred = @$q.defer()

        @$http.post('/user', user)
        .success((data, status, headers) =>
                @$log.info("Successfully created User - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create user - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    updateUser: (user, id) ->
        @$log.debug "updateUser #{angular.toJson(user, true)}"
        deferred = @$q.defer()

        @$http.post("/user/#{id}", user)
        .success((data, status, headers) =>
                @$log.info("Successfully updated User - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to update user - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('UserService', UserService)