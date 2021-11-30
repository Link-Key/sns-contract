// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

contract test{
    function getNow() public view returns (uint256){
        return block.timestamp;
    }
    
    function getSeconds() public pure returns (uint256){
        return 1 seconds;
    }
    
    function getMinutes() public pure returns (uint256){
        return 1 minutes;
    }
    
    function getHours() public pure returns (uint256){
        return 1 hours;
    }
    
    function getDays() public pure returns (uint256){
        return 1 days;
    }
    
    function getWeeks() public pure returns (uint256){
        return 1 weeks;
    }
   
   function test1() public pure returns (bool){
        bool flag;
        uint8[3] memory arr = [1,2,3];
        for(uint8 i = 0; i < arr.length; i++){
            if(arr[i]==3){
               flag = true; 
            }else{
                break;
            }
        }
        return flag;
   }
    
    // function getYears() public view returns (uint256){
    //     return 1 years;
    // }
    
}