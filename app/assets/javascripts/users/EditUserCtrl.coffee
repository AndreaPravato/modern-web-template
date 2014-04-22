
class EditUserCtrl

    constructor: (@$log, @$location,  @UserService, @$routeParams) ->
        @$log.debug "constructing EditUserController"
        @user = {}
        @dbContent = {}
        if @$routeParams.id  
            @UserService.getUser(@$routeParams.id)
            .then(
                (data) =>
                    @$log.debug "Promise returned a User"
                    @user = data
                    # @dbContent = data  # this is used in the noChange function 
                    # setting dbContent like that would make user and dbContent the same
                ,
                (error) =>
                    @$log.error "Unable to get User: #{error}"
                )            
            @UserService.getUser(@$routeParams.id)
            .then(
                (data) =>
                    @$log.debug "Promise returned a User"
                    @dbContent = data  # this is used in the noChange function 
                ,
                (error) =>
                    @$log.error "Unable to get User: #{error}"
                )            
        # retrieve the user (corresponding to id) from the database
        # @user.id.$oid is now populated; this will make the Delete button appear in the detail.html
        # TODO clone user in dbContent instead of making another HTTP call

    saveUser: () ->
        @$log.debug "saveUser() ..EditUserCtrl"
        @UserService.updateUser(@user, @$routeParams.id)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} User"
                @user = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to update User: #{error}"
            )

    # detects editing activity; used to trigger "Save changes" button in detail.html 
    noChange: () ->
        angular.equals @user, @dbContent
    
controllersModule.controller('EditUserCtrl', EditUserCtrl)
