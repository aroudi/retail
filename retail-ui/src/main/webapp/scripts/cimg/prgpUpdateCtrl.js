/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('prgpUpdateCtrl', function($scope,treeViewNode,command, $state, $timeout, baseDataService, SUCCESS, FAILURE, PRGP_ADD_TREEVIEW_URI, PRGP_UPDATE_TREEVIEW_URI, PRGP_GET_CATLIST_NOT_IN_DEPT_URI, PRGP_GET_CAT_TYPE_LIST_NOT_IN_DEPTCAT_URI, PRGP_GET_CATVALLIST_NOT_IN_DEPTCAT_URI ) {
    $scope.message = '';
    $scope.editMode = false;
    $scope.updateOnlyForThisGroup = true;
    $scope.currentNodeSetVisible = false;
    $scope.currentNodeValueVisible = true;
    $scope.nodeTitle = 'Department';
    $scope.parentLevel = '';
    $scope.currentNode = {id : -1};
    initPageData();
    function initPageData() {
        //remove un-necessary properties from object.
        trimTreeViewNodeObject(treeViewNode);

        if (command === 'addCategoryHeading') {
            $scope.nodeTitle = 'Category Heading';
            baseDataService.addRow(treeViewNode, PRGP_GET_CATLIST_NOT_IN_DEPT_URI).then(function(response) {
                $scope.currentNodeSet = response.data;
                if ($scope.currentNodeSet != undefined && $scope.currentNodeSet.length > 0) {
                    var newNode = { name: "--add new--", id : -1 };
                    $scope.currentNodeSet.unshift(newNode);
                    $scope.currentNodeSetVisible = true;
                    $scope.currentNodeValueVisible = true;
                    $scope.currentNode = baseDataService.populateSelectList($scope.currentNode,$scope.currentNodeSet);
                    //$scope.nodeValue = $scope.currentNode.name;
                }
            });
            baseDataService.addRow(treeViewNode, PRGP_GET_CAT_TYPE_LIST_NOT_IN_DEPTCAT_URI).then(function(response) {
                $scope.configCategorySet = response.data;
                $scope.categoryType = baseDataService.populateSelectList($scope.categoryType,$scope.configCategorySet);
            });
        }

        if (command === 'addCategoryValue') {
            $scope.nodeTitle = 'Category Value';
            baseDataService.addRow(treeViewNode, PRGP_GET_CATVALLIST_NOT_IN_DEPTCAT_URI).then(function(response) {
                $scope.currentNodeSet = response.data;
                if ($scope.currentNodeSet != undefined && $scope.currentNodeSet.length > 0) {
                    var newNode = { name: "--add new--", id : -1 };
                    $scope.currentNodeSet.unshift(newNode);
                    $scope.currentNodeSetVisible = true;
                    $scope.currentNodeValueVisible = true;
                    $scope.currentNode = baseDataService.populateSelectList($scope.currentNode,$scope.currentNodeSet);
                    //$scope.nodeValue = $scope.currentNode.name;
                }
            });
        }
        if (command === 'update') {
            if (treeViewNode.nodeType === 'DEPARTMENT') {
                $scope.nodeTitle = 'Department';
                $scope.editMode = false;
            }
            if (treeViewNode.nodeType === 'CATEGORY_HEADING') {
                $scope.editMode = false;
                $scope.parentLevel = 'Department';
            }
            if (treeViewNode.nodeType === 'CATEGORY_VALUE') {
                $scope.nodeTitle = 'Category Value';
                $scope.editMode = true;
                $scope.parentLevel = 'Category Heading';
            }
            $scope.nodeValue = treeViewNode.name;
        }
    };
    $scope.submit = function () {

        if (command === 'addCategoryHeading') {
            var newNode = {
                name: $scope.nodeValue,
                parentNodeId: treeViewNode.id,
                nodeType : 'CATEGORY_HEADING',
                categoryType : $scope.categoryType.categoryCode,
                id : $scope.currentNode.id
            };
        }
        if (command === 'addCategoryValue') {
            var newNode = {
                name: $scope.nodeValue,
                parentNodeId: treeViewNode.primaryKey,
                nodeType : 'CATEGORY_VALUE',
                id : $scope.currentNode.id
            };
        }
        if (command === 'addDepartment') {
            var newNode = {
                name: $scope.nodeValue,
                parentNodeId: -1,
                nodeType : 'DEPARTMENT',
                id : -1
            };
        }
        if (command === 'update') {
            var newNode = treeViewNode;
            newNode.name = $scope.nodeValue;
            newNode.order = $scope.updateOnlyForThisGroup ? -1 : 1;
        }
        //UPDATE
        if (command === 'update') {
            baseDataService.addRow(newNode, PRGP_UPDATE_TREEVIEW_URI).then(function(response) {
                var res = response.data;
                if (res === undefined || res === null) {
                    $scope.message = 'Product Group update failed!!!. please refer to the logs';
                } else if (res.id === -1) {
                    $scope.message = res.name;
                } else {
                    $scope.confirm(response.data);
                }
            });
        //ADD NEW NODE
        } else {
            baseDataService.addRow(newNode, PRGP_ADD_TREEVIEW_URI).then(function(response) {
                var res = response.data;
                if (res === undefined || res === null) {
                    $scope.message = 'Product Group update failed!!!. please refer to the logs';
                } else if (res.id === -1) {
                    $scope.message = res.name;
                } else {
                    $scope.confirm(response.data);
                }
            });
        }

    };
    $scope.cancel = function() {
        //$scope.$destroy();
        $scope.closeThisDialog('button');
    };

    $scope.changeCurrentNodeValue = function () {
        if ($scope.currentNode.id === -1) {
            $scope.currentNodeValueVisible = true;
            $scope.nodeValue = '';
        } else {
            $scope.currentNodeValueVisible = false;
            $scope.nodeValue = $scope.currentNode.name;
        }
        console.log('$scope.nodeValue =' + $scope.nodeValue);

    }
    function trimTreeViewNodeObject(treeViewNode) {
        delete treeViewNode.parentId;
        delete treeViewNode.nodeId;
        delete treeViewNode.selected;
        delete treeViewNode.expanded;
        delete treeViewNode.children;
    }
});
