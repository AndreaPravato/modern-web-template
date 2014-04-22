
class EditUserCtrl

    constructor: (@$log, @$location,  @UserService) ->
        @$log.debug "constructing EditUserController"
        @user = {}

    getUser: () ->
        @$log.debug "@id"

        # @UserService.listUsers()
        # .then(
        #     (data) =>
        #         @$log.debug "Promise returned #{data.length} Users"
        #         @users = data
        #     ,
        #     (error) =>
        #         @$log.error "Unable to get Users: #{error}"
        #     )
    

    # createUser: () ->
    #     @$log.debug "createUser()"
    #     @user.active = true
    #     @UserService.createUser(@user)
    #     .then(
    #         (data) =>
    #             @$log.debug "Promise returned #{data} User"
    #             @user = data
    #             @$location.path("/")
    #         ,
    #         (error) =>
    #             @$log.error "Unable to create User: #{error}"
    #         )

controllersModule.controller('EditUserCtrl', EditUserCtrl)