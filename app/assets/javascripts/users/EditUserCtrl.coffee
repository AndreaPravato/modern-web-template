
class EditUserCtrl

    constructor: (@$log, @$location,  @UserService, @$routeParams) ->
        @$log.debug "constructing EditUserController"
        @user = {}
        if @$routeParams.id  
            @UserService.getUser(@$routeParams.id)
            .then(
                (data) =>
                    @$log.debug "Promise returned a User"
                    @user = data
                    @dbContent = data  # this is used in the noChange function 
                ,
                (error) =>
                    @$log.error "Unable to get User: #{error}"
                )            
        # retrieve the user (corresponding to id) from the database
        # @user.id.$oid is now populated; this will make the Delete button appear in the detail.html

    createUser: () ->
        @$log.debug "createUser()"
        @user.active = true
        @UserService.createUser(@user)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} User"
                @user = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create User: #{error}"
            )

    
controllersModule.controller('EditUserCtrl', EditUserCtrl)