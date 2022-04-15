// vue.config.js
module.exports = {
    chainWebpack: config => {
        config
            .plugin('html')
            .tap(args => {
                args[0].title= 'TeamC便捷管理系统'
                return args
            })
    }
}
