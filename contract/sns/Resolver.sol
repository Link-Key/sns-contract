// SPDX-License-Identifier: MIT
pragma solidity >=0.8.4;
pragma experimental ABIEncoderV2;

/**
 * A generic resolver interface which includes all the functions including the ones deprecated
 */
interface Resolver {

    event ContentChanged(string indexed name_, string type_, string newContent);
    event OwnerChanged(string indexed name_, address newOwner);

    function setRecords(string memory name_, address owner_) external;

    function getEthAddress(string memory name_) external view returns (string memory);

    function getBtcAddress(string memory name_) external view returns (string memory);

    function getLtcAddress(string memory name_) external view returns (string memory);

    function getDogeAddress(string memory name_) external view returns (string memory);

    function getIpfsUrl(string memory name_) external view returns (string memory);

    function getUrl(string memory name_) external view returns (string memory);

    function getEmail(string memory name_) external view returns (string memory);

    function getAvatar(string memory name_) external view returns (string memory);

    function getDescription(string memory name_) external view returns (string memory);

    function getNotice(string memory name_) external view returns (string memory);

    function getKeywords(string memory name_) external view returns (string memory);

    function getComGithub(string memory name_) external view returns (string memory);

    function getComReddit(string memory name_) external view returns (string memory);

    function getComTwitter(string memory name_) external view returns (string memory);

    function getOrgTelegram(string memory name_) external view returns (string memory);

    function getAllProperties(string memory name_) external returns (string memory);

    function setAllProperties(string memory name_, string memory recordsStr_) external;

}
