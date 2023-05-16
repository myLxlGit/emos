package com.example.emos.wx.common.util.base.dto.core

import com.example.emos.wx.common.util.base.dto.constant.*

/**
 * 全局统一返回实体
 *
 * @author lxl
 * @date 2020/09/21 16:12
 */
class ResultT<T>: ResultInfo {
    /**
     * true 请求成功
     *
     * false 请求失败
     */
    var successful : Boolean? = null

    /**
     * 200 成功，500失败
     *
     * 返回值编码，其他具体有意义的值请参考
     *
     * **See also** [com.sumec.itc.basic.dto.constant.ResultCode](com.sumec.itc.basic.dto.constant.ResultCode)
     */
    val code: Int

    /**
     * 提示消息
     */
    val msg: String

    /**
     * 分页时数据量总数
     */
    var total: Long = -1

    /**
     * 返回数据，若为分页接口，返回值必为List，对应到前端Array
     */
    var data: T? = null
        private set

    internal constructor(code: Int, msg: String): this(code, msg, null) {}

    constructor() {
        code = INITIALIZATION_FAILURE
        msg = INITIALIZATION_FAILED
    }
    /**
     * 初始化一个新创建的 ResultT 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    internal constructor(code: Int, msg: String, data: T?) {
        this.code = code
        this.msg = msg
        this.data = data
        successful = code == 200
    }

    companion object {

        /**
         * 通用返回值
         *
         * @param msg String? 提示文本
         * @param data T? 数据
         * @param code Int? 编码 一般成功200/失败500
         * @return ResultT<T>
         */
        @JvmStatic
        @JvmOverloads
        fun <T> success(msg: String? = "成功", data: T? = null, code: Int? = SUCCESS): ResultT<T> {
            return ResultT(code ?: SUCCESS, msg ?: "成功", data).apply { successful = true }
        }

        /**
         * 返回成功数据
         *
         * @return 成功消息
         */
//        @Deprecated(
//            message = "以前版本泛型与字符串类型冲突，已对冲突方法进行重构，为了不影响已有代码，暂时先标记为废弃，后续会删除",
//            replaceWith = ReplaceWith("ResultT.success(msg, data)", "com.sumec.itc.basic.dto.core.ResultT")
//        )
        @JvmStatic
        fun <T> success(data: T?): ResultT<T> {
            return success("成功", data)
        }

        /**
         * 返回错误消息
         *
         * @return
         */
        @JvmStatic
        fun <T> error(): ResultT<T> {
            return error("失败")
        }

        /**
         * 返回错误消息
         *
         * @param msg 返回内容
         * @return 警告消息
         */
        @JvmStatic
        fun <T> error(msg: String): ResultT<T> {
            return error(msg, null)
        }

        /**
         * 返回错误消息
         *
         * @param msg  返回内容
         * @param data 数据对象
         * @return 警告消息
         */
        @JvmStatic
        fun <T> error(msg: String, data: T?): ResultT<T> {
            return ResultT(ERROR, msg, data).apply { successful = false }
        }

        /**
         * 返回错误消息
         *
         * @param code 状态码
         * @param msg  返回内容
         * @return 警告消息
         */
        @JvmStatic
        fun <T> error(code: Int, msg: String): ResultT<T> {
            return ResultT<T>(code, msg, null).apply { successful = false }
        }

        /**
         * 返回错误消息
         *
         * @param code 状态码
         * @param msg  返回内容
         * @param data  数据
         * @return 警告消息
         */
        @JvmStatic
        fun <T> error(code: Int, msg: String, data: T): ResultT<T> {
            return ResultT(code, msg, data).apply { successful = false }
        }

        @JvmStatic
        fun <T> count(count: Int): ResultT<T> {
            return result(count > 0)
        }

        @JvmStatic
        @JvmOverloads
        fun <T> result(result: Boolean, msg: String? = null): ResultT<T> {
            return if (result) {
                success<T>((msg ?: "") + "成功").apply { successful = true }
            } else {
                error<T>((msg ?: "") + "失败").apply { successful = false }
            }
        }

        /**
         * 如果数据不为空返回success，否则返回error
         *
         * @param result T? 数据
         * @return ResultT<T?>
         */
        @JvmStatic
        fun <T> exists(result: T?): ResultT<T> {
            return if (result == null) {
                error("数据不存在")
            } else {
                success("成功", result)
            }
        }
    }

    override fun toString(): String {
        return "ResultT(code=$code, msg='$msg', data=$data, successful=$successful)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResultT<*>) return false

        if (code != other.code) return false
        if (msg != other.msg) return false
        if (data != other.data) return false
        if (successful != other.successful) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + msg.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (successful.hashCode())
        return result
    }

}
/**
 *
 *
 * @author lxl
 * @date 2023/5/15 16:01
 */