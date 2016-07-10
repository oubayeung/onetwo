/***
 * @name: findRolesByUser
 * @parser: template
 * 
 */
select 
    tb.ID
    , tb.NAME
    , tb.STATUS
    , tb.REMARK
    , tb.CREATE_AT
    , tb.UPDATE_AT
    , tb.APP_CODE
from 
    admin_role tb
    left join 
        admin_user_role aur on tb.id=aur.role_id
where aur.user_id = :userId


/***
 * @name: insertUserRole
 * @parser: template
 * 
 */
    insert into admin_user_role (user_id, role_id) values (:userId, :roleId)

/***
 * @name: deleteUserRoles
 * @parser: template
 * 
 */
    delete from admin_user_role where user_id = :userId

/***
 * @name: insertRolePermission
 * @parser: template
 * 
 */
    insert into admin_role_permission (role_id, permission_code) values (:roleId,  :permissionCode) 

/***
 * @name: deleteRolePermisssion
 * @parser: template
 * 
 */
    delete  admin_role_permission where (role_id, permission_code) in (
        select 
               arp.role_id, arp.permission_code 
        from admin_role_permission  arp     
        left join admin_permission ap on ap.code=arp.permission_code    
         where 
         role_id=:roleId and ap.app_code=:appCode
            [#if permissionCode?has_content>
                and permission_code=:permissionCode
            [/#if]
   )
    
/***
 * @name: findRolePermisssion
 * @parser: template
 * 
 */
    select 
        permission_code 
    from 
        admin_role_permission  arp
    left join admin_permission ap on ap.code=arp.permission_code
    where role_id=:roleId and ap.app_code=:appCode
    
    
    
    
    
    
    


