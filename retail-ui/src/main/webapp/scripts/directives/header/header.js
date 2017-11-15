'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */
angular.module('sbAdminApp')
	.directive('header',function(UserService){
        return {
            templateUrl:'scripts/directives/header/header.html',
            restrict: 'E',
            replace: true,
            controller: function ($scope,$rootScope) {
                $scope.userLoggedIn = '';
                $rootScope.$on('loginChanged', function (event, data) {
                    $scope.userLoggedIn =  data;
                })
            }
        }
	});


