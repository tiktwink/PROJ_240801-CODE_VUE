const divideContentToSections = (html, symbol) => {
  let htmlcopy = html.slice(0) //确保是复制而非共享
  let sections = []
  let item = {}
  let response = {}
  let firstPartIsSectionContentNotPartContent = false
  let mayLackRightPad = false
  
  let si = html.indexOf(symbol)
  if (si === -1) {
    response.isSectioned = false
    response.sectionContent = html
    response.sections = sections
    return response
  }
  
  response.isSectioned = true
  response.sectionContent = html //当文章已分节时，是否要将整篇文章内容添加为返回体一级属性，注释掉表示不添加
  let sj = html.lastIndexOf('<', si) //当前#!所在标签头的左<
  if (sj !== 0) { //章首存在一个没有节标题的节
    // if (symbol === '#*') {
    //   firstPartIsSectionContentNotPartContent = true //对节分段时，第一段如果没有#*标记，则不能像对章分节那样处理，而应该依旧把这段标记为节而非段，这一部分是节的正文
    // }
    let sectionContent = html.slice(0, sj)
    item.sectionOrder = sections.length + 1
    item.sectionTitle = 'noneTitle'
    // item.sectionTitle = 'none' + sections.length + 1
    item.sectionContent = sectionContent
    item.firstSectionNoneMark = true
    sections.push(item)
    item = {}
    html = html.slice(sj)
  }
  
  si = html.indexOf(symbol)
  let sii = 0
  let sjj = 0
  while (si !== -1) {
    sj = html.indexOf('<', si) //当前si所指#!所在标签尾的左<
    item.sectionOrder = sections.length + 1
    item.sectionTitle = html.slice(si + 2, sj).trim()
    if (item.sectionTitle === '') {
      item.sectionTitle = 'noneTitle'
    }
    sj = html.indexOf('>', si) //当前si所指#!所在标签尾的右>
    if (symbol === '#!') {
      sii = html.indexOf('!#', si + 1)
    } else {
      sii = html.indexOf('*#', si + 1)
    }
    if (sii === -1) {
      // console.log('sii===-1')
      item.sectionContent = html.slice(sj + 1)
      sections.push(item)
      item = {}
      si = -1 //终止匹配过程
    } else {
      sjj = html.lastIndexOf('<', sii)//当前sii所指!#或*#所在标签尾的左<
      // console.log(sjj)
      item.sectionContent = html.slice(sj + 1, sjj)
      sections.push(item)
      item = {}
      html = html.slice(sii + 2)
      si = html.indexOf(symbol)
    }
  }
  //终止匹配，但这只是“可能”是最后一个节（段），因为最后一节（段）可以不加#!!#和#**#
  if (symbol === '#!') {
    // console.log('将对节进行判断是否最后一节')
    si = htmlcopy.lastIndexOf('!#')
    sj = htmlcopy.indexOf('>', si + 2)
    if (htmlcopy.slice(sj + 1).trim() === '') {
      // console.log('这是最后一节了')
    } else {
      // console.log('这是不是最后一节')
      item.isSectioned = false
      item.sectionContent = htmlcopy.slice(sj + 1)
      item.sectionOrder = sections.length + 1
      item.sectionTitle = 'noneTitle'
      item.lastSectionNoneMark = true
      // item.sectionClass=['article-section']
      sections.push(item)
    }
  } else if (symbol === '#*') {
    // console.log('将对段进行判断是否最后一段')
    si = htmlcopy.lastIndexOf('*#')
    // console.log(si)
    sj = htmlcopy.indexOf('>', si)
    // console.log(sj)
    if (htmlcopy.slice(sj + 1).trim() === '') {
      // console.log('这是最后一段了')
    } else {
      // console.log('这是不是最后一段，后续将标记为节而非段')
      item.sectionContent = htmlcopy.slice(sj + 1)
      // console.log(item.sectionContent)
      item.sectionTitle = 'noneTitle'
      item.sectionOrder = sections.length + 1
      item.lastSectionNoneMark = true
      sections.push(item)
    }
  }
  
  // 解析标题
  sections = sections.map((item, index) => {
    return {
      ...item,
      ...viewSectionTitle(item, symbol)
    }
  })
  
  response.sections = sections
  response.sectionCount = sections.length
  return response
}

//解析标题（节、段）
const viewSectionTitle = (item, symbol) => {
  let sectionTitle = item.sectionTitle
  let sectionClass = []
  if (symbol === '#!') {
    sectionClass = ['article-section']
    
    if (sectionTitle.includes('#1')) {
      sectionClass.push('article-section-fantacy')
      sectionTitle = sectionTitle.replace('#1', '')
    }
    if (sectionTitle.includes('#2')) {
      sectionClass.push('article-section-colorful')
      sectionTitle = sectionTitle.replace('#2', '')
    }
    if (sectionTitle.includes('/1')) {
      sectionClass.push('article-section-pre')
      sectionTitle = sectionTitle.replace('/1', '')
    }
    if (sectionTitle.includes('/2')) {
      sectionClass.push('article-section-ad')
      sectionTitle = sectionTitle.replace('/2', '')
    }
  } else if (symbol === '#*') {
    if (item.firstSectionNoneMark || item.lastSectionNoneMark) {
      sectionClass = ['article-section']
    } else {
      sectionClass = ['article-part']
    }
  }
  // console.log('class: ' + sectionClass)
  return {
    sectionTitle: sectionTitle,
    sectionClass: sectionClass
  }
}

//默认导出
const divideToSections = (html) => {
  let response = divideContentToSections(html, '#!') //解析节标题时，symbol为#!
  if (!response.isSectioned) {
    return response
  }
  let sections = response.sections
  sections = sections.map((item, index) => {
    return {
      ...item,
      ...divideContentToSections(item.sectionContent, '#*'), //解析段标题时，symbol为#*
    }
  })
  response.sections = sections
  return response
}

export default divideToSections
