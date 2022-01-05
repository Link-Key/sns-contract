// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./Resolver.sol";

contract SNSResolver is Resolver {
    using LibString for string;

    struct Record {
        address owner;
        //address
        string ethAddress;
        string btcAddress;
        string ltcAddress;
        string dogeAddress;
        //ipfs
        string ipfsUrl;
        //records
        string url;
        string email;
        string avatar;
        string description;
        string notice;
        string keywords;
        string comGithub;
        string comReddit;
        string comTwitter;
        string orgTelegram;
    }

    mapping(string => Record) private records;

    mapping(string => string) private recordsStr;

    modifier authorised(string memory name) {
        address owner = records[name].owner;
        require(owner == msg.sender);
        _;
    }

    modifier onlySNS(address snsAddress_) {
        require(snsAddress_ == _snsAddress);
        _;
    }

    address private _snsAddress;



    /**
     * @dev Constructs a new SNS.
     */
    constructor(address snsAddress_) public {
        _snsAddress = snsAddress_;
    }


    function setRecords(string memory name_, address owner_) public onlySNS(msg.sender) {
        records[name_].owner = owner_;
        emit OwnerChanged(name_, owner_);
    }

    function getEthAddress(string memory name_) external view returns (string memory) {
        return records[name_].ethAddress;
    }

    function getBtcAddress(string memory name_) external view returns (string memory){
        return records[name_].btcAddress;
    }

    function getLtcAddress(string memory name_) external view returns (string memory){
        return records[name_].ltcAddress;
    }

    function getDogeAddress(string memory name_) external view returns (string memory){
        return records[name_].dogeAddress;
    }

    function getIpfsUrl(string memory name_) external view returns (string memory){
        return records[name_].ipfsUrl;
    }

    function getUrl(string memory name_) external view returns (string memory){
        return records[name_].url;
    }

    function getEmail(string memory name_) external view returns (string memory){
        return records[name_].email;
    }

    function getAvatar(string memory name_) external view returns (string memory){
        return records[name_].avatar;
    }

    function getDescription(string memory name_) external view returns (string memory){
        return records[name_].description;
    }

    function getNotice(string memory name_) external view returns (string memory){
        return records[name_].notice;
    }

    function getKeywords(string memory name_) external view returns (string memory){
        return records[name_].keywords;
    }

    function getComGithub(string memory name_) external view returns (string memory){
        return records[name_].comGithub;
    }

    function getComReddit(string memory name_) external view returns (string memory){
        return records[name_].comReddit;
    }

    function getComTwitter(string memory name_) external view returns (string memory){
        return records[name_].comTwitter;
    }

    function getOrgTelegram(string memory name_) external view returns (string memory){
        return records[name_].orgTelegram;
    }

    function setAllProperties(string memory name_, string memory recordsStr_) external authorised(name_) {
        recordsStr[name_] = recordsStr_;
        string[] memory properties = recordsStr_.split("+");
        require(properties.length == 15 || properties.length == 14, "013 --- SNS.sol --- setAllProperties --- recordsStr error!!!");
        records[name_].ethAddress = properties[0].equalNocase("")?records[name_].ethAddress:properties[0];
        records[name_].btcAddress = properties[1].equalNocase("")?records[name_].btcAddress:properties[0];
        records[name_].ltcAddress = properties[2].equalNocase("")?records[name_].ltcAddress:properties[0];
        records[name_].dogeAddress = properties[3].equalNocase("")?records[name_].dogeAddress:properties[0];
        records[name_].ipfsUrl = properties[4].equalNocase("")?records[name_].ipfsUrl:properties[0];
        records[name_].url = properties[5].equalNocase("")?records[name_].url:properties[0];
        records[name_].email = properties[6].equalNocase("")?records[name_].email:properties[0];
        records[name_].avatar = properties[7].equalNocase("")?records[name_].avatar:properties[0];
        records[name_].description = properties[8].equalNocase("")?records[name_].description:properties[0];
        records[name_].notice = properties[9].equalNocase("")?records[name_].notice:properties[0];
        records[name_].keywords = properties[10].equalNocase("")?records[name_].keywords:properties[0];
        records[name_].comGithub = properties[11].equalNocase("")?records[name_].comGithub:properties[0];
        records[name_].comReddit = properties[12].equalNocase("")?records[name_].comReddit:properties[0];
        records[name_].comTwitter = properties[13].equalNocase("")?records[name_].comTwitter:properties[0];
        if(properties.length == 15){
            records[name_].orgTelegram = properties[14].equalNocase("")?records[name_].orgTelegram:properties[0];
        }

        emit ContentChanged(name_, "all", recordsStr_);
    }

    function getAllProperties(string memory name_) external view returns (string memory){
        return recordsStr[name_];
    }

}