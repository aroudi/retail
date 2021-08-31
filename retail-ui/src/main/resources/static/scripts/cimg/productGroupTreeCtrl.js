/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productGroupTreeCtrl', function($scope, baseDataService, ngDialog, SUCCESS, FAILURE, PRGP_GET_ALL_TREEVIEW_URI, PRGP_DELETE_TREEVIEW_URI) {
    //$scope.basicTree = [{ name: "Node 1", id:'1', children: [{ name: "Node 1.1", id:'1.1', children: [{ name: "Node 1.1.1" }, { name: "Node 1.1.2" }] }] },
      //  { name: "Node 2", id:'2',children: [{ name: "Node 2.1" }, { name: "Node 2.2" }] }]
    $scope.addButtonVisible = false;
    $scope.AddButtonTitle = '';
    $scope.updateCommand = '';
    $scope.$on('selection-changed', function(e, node) {
        $scope.editButtonVisible = true;
        console.log('selection-changed called');
        $scope.selectedNode = node;
        if ($scope.selectedNode.nodeType === 'DEPARTMENT') {
            $scope.addButtonVisible = true;
            $scope.AddButtonTitle = 'Add Category Heading';
            $scope.updateCommand = 'addCategoryHeading';
        } else if ($scope.selectedNode.nodeType === 'CATEGORY_HEADING') {
            $scope.addButtonVisible = true;
            $scope.AddButtonTitle = 'Add Category Value';
            $scope.updateCommand = 'addCategoryValue';
        } else {
            $scope.addButtonVisible = false;
        }
    })

    loadProductGroups();
    function loadProductGroups() {
        baseDataService.getBaseData(PRGP_GET_ALL_TREEVIEW_URI).then(function(response){
            //var data = angular.copy(response.data);
            $scope.productGroupTree = response.data;
        });
    }

    $scope.addNode = function (newCommand) {
        //dont allow to add more than 3 categories
        if (newCommand === 'addCategoryHeading') {
            if ($scope.selectedNode.children.length ===3) {
                baseDataService.displayMessage('info', 'Warning','you are not allowed to create more than 3 category heading');
                return;
            }
        }
        ngDialog.openConfirm({
            template:'views/pages/prgpUpdate.html',
            controller:'prgpUpdateCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {treeViewNode: function(){return angular.copy($scope.selectedNode)}, command:function(){return newCommand}}
        }).then (function (result){
                //add the return node to the children of current node.
            loadProductGroups();
            /*
            if (newCommand === 'addDepartment' ) {
                $scope.productGroupTree.push(result);
            } else {
                $scope.selectedNode.children.push(result);
            }
            */
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );

    }
    $scope.editNode = function () {
        ngDialog.openConfirm({
            template:'views/pages/prgpUpdate.html',
            controller:'prgpUpdateCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {treeViewNode: function(){return angular.copy($scope.selectedNode)}, command:function(){return 'update'}}
        }).then (function (result){
                //add the return node to the children of current node.
                loadProductGroups();
                /*
                 if (newCommand === 'addDepartment' ) {
                 $scope.productGroupTree.push(result);
                 } else {
                 $scope.selectedNode.children.push(result);
                 }
                 */
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );

    };

    $scope.deleteProductGroup = function () {
        if ($scope.selectedNode === undefined) {
            return;
        }
        baseDataService.displayMessage('yesNo','Warnning','Proceed with delete?').then(function(result){
            if (result) {
                if ($scope.selectedNode.nodeType === 'DEPARTMENT' && $scope.selectedNode.name.toLowerCase() ==='default' ) {
                    baseDataService.displayMessage('info', 'Error','You can not delete DEFAULT department');
                    return;
                }
                trimTreeViewNodeObject($scope.selectedNode);
                baseDataService.addRow($scope.selectedNode, PRGP_DELETE_TREEVIEW_URI).then(function(response) {
                    var res = response.data;
                    if (res.status === -1) {
                        baseDataService.displayMessage('info', 'Error','Not able to delete product group ' + res.message);
                    } else{
                        loadProductGroups();
                    }
                });
            } else {
                return;
            }
        });
    };

    function trimTreeViewNodeObject(treeViewNode) {
        delete treeViewNode.parentId;
        delete treeViewNode.nodeId;
        delete treeViewNode.selected;
        delete treeViewNode.expanded;
        delete treeViewNode.children;
    }

});
